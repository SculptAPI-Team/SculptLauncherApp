package com.xbox.httpclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes2-dex2jar.jar:com/xbox/httpclient/NetworkObserver.class */
public class NetworkObserver {
    private static String s_lastCapabilities = "";
    private static String s_lastLinkProperties = "";
    private static ConnectivityManager.NetworkCallback s_networkChangedCallback = new ConnectivityManager.NetworkCallback() { // from class: com.xbox.httpclient.NetworkObserver.1
        private void LogMessage(Network network, String str) {
            NetworkObserver.Log("Network ID " + network.hashCode() + " " + str);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            LogMessage(network, "is available");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            String strCheckNetworkCapabilities = NetworkDetails.checkNetworkCapabilities(networkCapabilities);
            if (strCheckNetworkCapabilities.equals(NetworkObserver.s_lastCapabilities)) {
                return;
            }
            String unused = NetworkObserver.s_lastCapabilities = strCheckNetworkCapabilities;
            LogMessage(network, "has capabilities: " + NetworkObserver.s_lastCapabilities);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            String strCheckLinkProperties = NetworkDetails.checkLinkProperties(linkProperties);
            if (strCheckLinkProperties.equals(NetworkObserver.s_lastLinkProperties)) {
                return;
            }
            String unused = NetworkObserver.s_lastLinkProperties = strCheckLinkProperties;
            LogMessage(network, "has link properties: " + NetworkObserver.s_lastLinkProperties);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            LogMessage(network, "was lost");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            NetworkObserver.Log("No networks were available");
        }
    };

    /* loaded from: classes2-dex2jar.jar:com/xbox/httpclient/NetworkObserver$NetworkDetails.class */
    static class NetworkDetails {
        NetworkDetails() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String checkLinkProperties(LinkProperties linkProperties) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("hasProxy", Boolean.valueOf(linkProperties.getHttpProxy() != null));
            return join(linkedHashMap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String checkNetworkCapabilities(NetworkCapabilities networkCapabilities) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("isWifi", Boolean.valueOf(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)));
            linkedHashMap.put("isBluetooth", Boolean.valueOf(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)));
            linkedHashMap.put("isCellular", Boolean.valueOf(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)));
            linkedHashMap.put("isVpn", Boolean.valueOf(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)));
            linkedHashMap.put("isEthernet", Boolean.valueOf(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)));
            linkedHashMap.put("shouldHaveInternet", Boolean.valueOf(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)));
            linkedHashMap.put("isNotVpn", Boolean.valueOf(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)));
            linkedHashMap.put("internetWasValidated", Boolean.valueOf(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)));
            if (Build.VERSION.SDK_INT >= 28) {
                linkedHashMap.put("isNotSuspended", Boolean.valueOf(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)));
            }
            return join(linkedHashMap);
        }

        static String getNetworkDetails(Network network, ConnectivityManager connectivityManager) {
            StringBuilder sb = new StringBuilder("Network ");
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
            return sb.append(network.hashCode()).append(":\n  Capabilities: ").append(networkCapabilities != null ? checkNetworkCapabilities(networkCapabilities) : "Got null capabilities").append("\n  Link properties: ").append(linkProperties != null ? checkLinkProperties(linkProperties) : "Got null link properties").toString();
        }

        private static String join(Map<String, Boolean> map) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(entry.getKey()).append(": ").append(entry.getValue());
            }
            return sb.toString();
        }
    }

    public static void Cleanup(Context context) {
        ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).unregisterNetworkCallback(s_networkChangedCallback);
    }

    public static void Initialize(Context context) {
        ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).registerNetworkCallback(new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(), s_networkChangedCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void Log(String str);
}
