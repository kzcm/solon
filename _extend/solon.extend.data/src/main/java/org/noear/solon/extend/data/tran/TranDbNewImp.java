package org.noear.solon.extend.data.tran;

import org.noear.solon.core.Tran;
import org.noear.solon.core.TranSession;
import org.noear.solon.ext.RunnableEx;
import org.noear.solon.extend.data.TranLocal;

public class TranDbNewImp extends DbTran implements Tran {
    public TranDbNewImp(TranSession session) {
        super(session);
    }

    @Override
    public void apply(RunnableEx runnable) throws Throwable {
        //尝试挂起事务
        //
        DbTran tran = TranLocal.trySuspend();

        try {
            super.execute(() -> {
                runnable.run();
            });
        } finally {
            //尝试恢复事务
            TranLocal.tryResume(tran);
        }
    }
}