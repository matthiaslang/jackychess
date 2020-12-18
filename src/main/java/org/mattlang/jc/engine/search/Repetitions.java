package org.mattlang.jc.engine.search;

public class Repetitions {


/* reps() returns the number of times that the current
   position has been repeated. Thanks to John Stanback
   for this clever algorithm. */
//
//    int reps()
//    {
//        int i;
//        int b[64];
//        int c = 0;  /* count of squares that are different from
//				   the current position */
//        int r = 0;  /* number of repetitions */
//
//        /* is a repetition impossible? */
//        if (fifty <= 3)
//            return 0;
//
//        memset(b, 0, sizeof(b));
//
//        /* loop through the reversible moves */
//        for (i = hply - 1; i >= hply - fifty - 1; --i) {
//            if (++b[(int)hist_dat[i].m.b.from] == 0)
//                --c;
//            else
//                ++c;
//            if (--b[(int)hist_dat[i].m.b.to] == 0)
//                --c;
//            else
//                ++c;
//            if (c == 0)
//                ++r;
//        }
//
//        return r;
//    }

}
