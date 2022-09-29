package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.Constants.DEFAULT_CACHE_SIZE_MB;
import static org.mattlang.jc.engine.tt.LongCache.toFlag;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import org.mattlang.jc.Factory;
import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

/**
 * Cache using only a long array to be faster and more memory efficient.
 * It is also thread-safe (thread consistent for read access), so that it could be used for a Lazy-SMP Search algorithm.
 * It combines a "always save" with replace "lowest depth" in a multi bucket cache.
 *
 */
public final class TTCache3 implements TTCacheInterface {

	public static final long NORESULT = Long.MAX_VALUE;

	private static final Logger LOGGER = Logger.getLogger(TTCache3.class.getSimpleName());

	private static int POWER_2_TT_ENTRIES = 22;
	private static final int BUCKET_SIZE = 3;

	private int keyShifts;

	// key, value
	private long[] keys;

	private TTAging aging = new TTAging();

	public int halfMoveCounter = 0;

	/**
	 * 8: depth
	 * 8: flag
	 * 16+32 = 48: move
	 *
	 * */

	// ///////////////////// DEPTH //10 bits
	private static final int FLAG = 10; // 2
	private static final int MOVE = 12; // 22
	private static final int SCORE = 48; // 16

	private long cacheHits = 0;
	private long cacheMisses = 0;

	private int mbSize = DEFAULT_CACHE_SIZE_MB;

	private int determineBitSizeFromConfig() {
		int mb = getConfiguredMbSize();
		mbSize = mb;
		return determineCacheBitSizeFromMb(mb, 16);
	}

	private int getConfiguredMbSize() {
		Integer mb = Factory.getDefaults().getConfig().hash.getValue();

		if (mb == null) {
			return DEFAULT_CACHE_SIZE_MB;
		}
		return mb.intValue();
	}

	public static int determineCacheBitSizeFromMb(int mb, int sizeOfSlot) {
		int slots = mb * 1024 * 1024 / sizeOfSlot;
		int bits = (int) (Math.log(slots) / Math.log(2));
		LOGGER.info("cache of " + mb + "MB: setting cache to " + slots + " slots, " + bits + " bits");
		return bits;
	}

	public TTCache3() {
		initCache();
	}

	private void initCache() {
		int bitSize = determineBitSizeFromConfig();
		POWER_2_TT_ENTRIES = bitSize - BUCKET_SIZE + 1;

		keyShifts = 64 - POWER_2_TT_ENTRIES;
		int maxEntries = (int) (1L << POWER_2_TT_ENTRIES + BUCKET_SIZE - 1) * 2;
		LOGGER.info("TT Cache: allocating " + maxEntries + " longs;");

		keys = new long[maxEntries];
	}

	@Override
	public void reset() {
		Arrays.fill(keys, 0);
		checkUpdateCacheSize();
	}

	public long getValue(final long key) {

		final int index = getIndex(key);

		for (int i = index; i < index + BUCKET_SIZE * 2; i += 2) {
			long xorKey = keys[i];
			long value = keys[i + 1];
			if ((xorKey ^ value) == key) {
				cacheHits++;
				return value;
			}
		}

		cacheMisses++;
		return NORESULT;
	}

	private int getIndex(final long key) {
		return (int) (key >>> keyShifts) << 1;
	}



	public void addValue(final long key, int score, final int depth, final int flag, final int move) {

		final int index = getIndex(key);
		long replacedDepth = Integer.MAX_VALUE;
		int replaceIndex = index;
		for (int i = index; i < index + BUCKET_SIZE * 2; i += 2) {

			long xorKey = keys[i];
			if (xorKey == 0) {
				replaceIndex = i;
				break;
			}

			long currentValue = keys[i + 1];

			int currentDepth = getDepth(currentValue);
			if ((xorKey ^ currentValue) == key) {
				if (currentDepth > depth && flag != TTResult.EXACT_VALUE) {
					return;
				}
				replaceIndex = i;
				break;
			}

			// replace the lowest depth
			if (currentDepth < replacedDepth) {
				replaceIndex = i;
				replacedDepth = currentDepth;
			}
		}

		final long value = createValue(score, move, flag, depth);

		keys[replaceIndex] = key ^ value;
		keys[replaceIndex + 1] = value;
	}

	public static int getScore(final long value) {
		int score = (int) (value >> SCORE);
		return score;
	}

	public int getDepth(final long value) {
		return (int) ((value & 0x3ff) - halfMoveCounter);
	}

	public static int getFlag(final long value) {
		return (int) (value >>> FLAG & 3);
	}

	public static int getMove(final long value) {
		return (int) (value >>> MOVE & 0xffffffff);
	}

	// SCORE,HALF_MOVE_COUNTER,MOVE,FLAG,DEPTH
	public long createValue(final long score, final long move, final long flag, final int depth) {
		return score << SCORE | move << MOVE | flag << FLAG | (depth + halfMoveCounter);
	}

	public String toString(long ttValue) {
		return "score=" + getScore(ttValue) + /*" " + new MoveWrapper(getMove(ttValue)) +*/ " depth=" + getDepth(
				ttValue)
				+ " flag="
				+ getFlag(ttValue);
	}

	public long getUsagePercentage() {
		int usage = 0;
		for (int i = 0; i < 1000; i++) {
			if (keys[i] != 0) {
				usage++;
			}
		}
		return usage;
	}

	@Override
	public void resetStatistics() {

	}

	@Override
	public void collectStatistics(Map stats) {

	}

	@Override
	public boolean findEntry(TTResult result, BoardRepresentation board) {
		long v = getValue(board.getZobristHash());
		if (v != NORESULT) {
			result.setDepth(getDepth(v));
			result.setType((byte) getFlag(v));
			result.setScore(getScore(v));
			result.setMove(getMove(v));
			return true;
		}
		return false;
	}

	@Override
	public void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta, int depth,
			int move) {
		addValue(currBoard.getZobristHash(), max, depth, toFlag(max, alpha, beta), move);
	}

	@Override
	public void updateAging(BoardRepresentation board) {
		LOGGER.info("hits: " + cacheHits + "; fails:" + cacheMisses);
		halfMoveCounter = aging.updateAging(board);
		//		if (halfMoveCounter>512){
		//			halfMoveCounter=0;
		//		}
	}

	private void checkUpdateCacheSize() {
		if (getConfiguredMbSize() != mbSize) {
			initCache();
		}
	}

	@Override
	public long calcHashFull() {
		return getUsagePercentage();
	}

	@Override
	public boolean isUsableForLazySmp() {
		return true;
	}
}
