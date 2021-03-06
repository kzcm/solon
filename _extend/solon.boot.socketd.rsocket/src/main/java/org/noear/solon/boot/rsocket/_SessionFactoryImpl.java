package org.noear.solon.boot.rsocket;

import io.rsocket.RSocket;
import org.noear.solon.core.message.Session;
import org.noear.solon.extend.socketd.Connector;
import org.noear.solon.extend.socketd.SessionFactory;

import java.net.URI;

/**
 * @author noear 2020/12/14 created
 * @since 1.2
 */
class _SessionFactoryImpl implements SessionFactory {
    @Override
    public String[] schemes() {
        return new String[]{"tcp","rtcp"};
    }

    @Override
    public Class<?> driveType() {
        return RSocket.class;
    }

    @Override
    public Session createSession(Connector connector) {
        if (connector.driveType() == RSocket.class) {
            return new _SocketSession((Connector<RSocket>) connector);
        } else {
            throw new IllegalArgumentException("Only support Connector<RSocket> connector");
        }
    }

    @Override
    public Session createSession(URI uri, boolean autoReconnect) {
        return new _SocketSession(new RsConnector(uri, autoReconnect));
    }
}
