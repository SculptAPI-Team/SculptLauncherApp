package com.microsoft.xbox.idp.services;

public class EndpointsFactory {
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$xbox$idp$services$Endpoints$Type;

        static {
            int[] iArr = new int[Endpoints.Type.values().length];
            $SwitchMap$com$microsoft$xbox$idp$services$Endpoints$Type = iArr;
            try {
                iArr[Endpoints.Type.PROD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$microsoft$xbox$idp$services$Endpoints$Type[Endpoints.Type.DNET.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static Endpoints get() {
        int i = AnonymousClass1.$SwitchMap$com$microsoft$xbox$idp$services$Endpoints$Type[Config.endpointType.ordinal()];
        if (i == 1) {
            return new EndpointsProd();
        }
        if (i != 2) {
            return null;
        }
        return new EndpointsDnet();
    }
}
