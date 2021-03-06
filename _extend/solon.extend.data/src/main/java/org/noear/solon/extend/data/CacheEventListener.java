package org.noear.solon.extend.data;

import org.noear.solon.Utils;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.cache.CacheService;
import org.noear.solon.core.event.EventListener;

class CacheEventListener implements EventListener<BeanWrap> {
    @Override
    public void onEvent(BeanWrap bw) {
        if (bw.raw() instanceof CacheService) {
            if (Utils.isEmpty(bw.name())) {
                CacheLib.cacheServiceAdd("", bw.raw());
            } else {
                CacheLib.cacheServiceAddIfAbsent(bw.name(), bw.raw());

                if (bw.typed()) {
                    CacheLib.cacheServiceAdd("", bw.raw());
                }
            }
        }
    }
}
