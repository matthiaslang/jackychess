package org.mattlang.jc.engine.tt;

import static org.mattlang.jc.engine.tt.TTEntry.*;

import java.util.Arrays;
import java.util.Map;

import org.mattlang.jc.board.BoardRepresentation;
import org.mattlang.jc.board.Color;

/**
 * Experimentell cache using only a long array to be faster and more memory efficient.
 */
public final class TTCache3 implements TTCacheInterface {

	private static final int POWER_2_TT_ENTRIES = 22;
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

	/**
	 * offset to deal correctly with negative values. todo how to impl this with real unsigned arithmetic?
	 */
	private static final int DEPTH_OFFSET = 512;

	// ///////////////////// DEPTH //10 bits
	private static final int FLAG = 10; // 2
	private static final int MOVE = 12; // 22
	private static final int SCORE = 48; // 16

	private long cacheHits = 0;
	private long cacheMisses = 0;

	public TTCache3() {
		keyShifts = 64 - POWER_2_TT_ENTRIES;
		int maxEntries = (int) (1L << POWER_2_TT_ENTRIES + BUCKET_SIZE - 1) * 2;

		keys = new long[maxEntries];
	}

	public void clearValues() {
		Arrays.fill(keys, 0);
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
		return 0;
	}

	private int getIndex(final long key) {
		return (int) (key >>> keyShifts) << 1;
	}

	public static final int toFlag(int max, int alpha, int beta) {
		if (max <= alpha) // a lowerbound value
			return LOWERBOUND;
		else if (max >= beta) // an upperbound value
			return UPPERBOUND;
		else // a true minimax value
			return EXACT_VALUE;
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
				if (currentDepth > depth && flag != TTEntry.EXACT_VALUE) {
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
		//TESTTESTTESTTEST
		if (getScore(value) != score) {
			throw new IllegalStateException(
					"score problem with " + score + " move:" + move + " flag:" + flag + " depth:" + depth
							+ " halfmovecounter:" + halfMoveCounter);
		}

		keys[replaceIndex] = key ^ value;
		keys[replaceIndex + 1] = value;
	}

	public static int getScore(final long value) {
		int score = (int) (value >> SCORE);
		return score;
	}

	public int getDepth(final long value) {
		return (int) ((value & 0x3ff) - DEPTH_OFFSET - halfMoveCounter);
	}

	public static int getFlag(final long value) {
		return (int) (value >>> FLAG & 3);
	}

	public static int getMove(final long value) {
		return (int) (value >>> MOVE & 0xffffffff);
	}

	// SCORE,HALF_MOVE_COUNTER,MOVE,FLAG,DEPTH
	public long createValue(final long score, final long move, final long flag, final int depth) {
		return score << SCORE | move << MOVE | flag << FLAG | (depth + DEPTH_OFFSET + halfMoveCounter);
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

	public static boolean canRefineEval(final long ttValue, final int eval, final int score) {
		if (ttValue != 0) {
			if (getFlag(ttValue) == TTEntry.EXACT_VALUE
					|| getFlag(ttValue) == TTEntry.UPPERBOUND && score < eval
					|| getFlag(ttValue) == TTEntry.LOWERBOUND && score > eval) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void resetStatistics() {

	}

	@Override
	public void collectStatistics(Map stats) {

	}

	private final TTEntry leightweightEntry = new TTEntry(0L, 0, (byte) 0, 0, (byte) 0, 0);

	@Override
	public TTEntry getTTEntry(BoardRepresentation board, Color side) {
		long v = getValue(board.getZobristHash());
		if (v != 0) {
			leightweightEntry.update(board.getZobristHash(), getScore(v), (byte) getFlag(v), getDepth(v), (byte) 0,
					getMove(v));
			return leightweightEntry;
		} else {
			return null;
		}
	}

	@Override
	public void storeTTEntry(BoardRepresentation currBoard, Color color, int max, int alpha, int beta, int depth,
			int move) {
		addValue(currBoard.getZobristHash(), max, depth, toFlag(max, alpha, beta), move);
	}

	@Override
	public void updateAging(BoardRepresentation board) {
		halfMoveCounter = aging.updateAging(board);
	}

	@Override
	public long calcHashFull() {
		return getUsagePercentage();
	}
}
