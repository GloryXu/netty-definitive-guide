package netty.jboss.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.*;

public class MarshallingCodeCFactory {

    public static MarshallingDecoder buildMarshallingDecoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);

        return decoder;
    }

    public static MarshallingEncoder buildMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);

        return encoder;
    }

    public static Unmarshaller buildUnMarshalling() {
        return  null;
    }

    public static Marshaller buildMarshalling() {
        return null;
    }
}
