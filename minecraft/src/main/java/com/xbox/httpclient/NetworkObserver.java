package com.xbox.httpclient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class NetworkObserver {
    private static String s_lastCapabilities = "";
    private static String s_lastLinkProperties = "";
    private static ConnectivityManager.NetworkCallback s_networkChangedCallback = new ConnectivityManager.NetworkCallback() { // from class: com.xbox.httpclient.NetworkObserver.1
        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(@NonNull Network network) {
            LogMessage(network, "is available");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(@NonNull Network network) {
            LogMessage(network, "was lost");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            NetworkObserver.Log("No networks were available");
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            String strCheckNetworkCapabilities = NetworkDetails.checkNetworkCapabilities(networkCapabilities);
            if (strCheckNetworkCapabilities.equals(NetworkObserver.s_lastCapabilities)) {
                return;
            }
            String unused = NetworkObserver.s_lastCapabilities = strCheckNetworkCapabilities;
            LogMessage(network, "has capabilities: " + NetworkObserver.s_lastCapabilities);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
            String strCheckLinkProperties = NetworkDetails.checkLinkProperties(linkProperties);
            if (strCheckLinkProperties.equals(NetworkObserver.s_lastLinkProperties)) {
                return;
            }
            String unused = NetworkObserver.s_lastLinkProperties = strCheckLinkProperties;
            LogMessage(network, "has link properties: " + NetworkObserver.s_lastLinkProperties);
        }

        private void LogMessage(Network network, String str) {
            NetworkObserver.Log("Network ID " + network.hashCode() + " " + str);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static native void Log(String str);

    public static void Initialize(Context context) {
        ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).registerNetworkCallback(new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(), s_networkChangedCallback);
    }

    public static void Cleanup(Context context) {
        ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).unregisterNetworkCallback(s_networkChangedCallback);
    }

    static class NetworkDetails {
        NetworkDetails() {
        }

        static String getNetworkDetails(Network network, ConnectivityManager connectivityManager) {
            StringBuilder sb = new StringBuilder("Network ");
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
            return sb.append(network.hashCode()).append(":\n  Capabilities: ").append(networkCapabilities != null ? checkNetworkCapabilities(networkCapabilities) : "Got null capabilities").append("\n  Link properties: ").append(linkProperties != null ? checkLinkProperties(linkProperties) : "Got null link properties").toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String checkNetworkCapabilities(NetworkCapabilities networkCapabilities) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("isWifi", networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            linkedHashMap.put("isBluetooth", networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
            linkedHashMap.put("isCellular", networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            linkedHashMap.put("isVpn", networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN));
            linkedHashMap.put("isEthernet", networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            linkedHashMap.put("shouldHaveInternet", networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
            linkedHashMap.put("isNotVpn", networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN));
            linkedHashMap.put("internetWasValidated", networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED));
            if (Build.VERSION.SDK_INT >= 28) {
                linkedHashMap.put("isNotSuspended", networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED));
            }
            return join(linkedHashMap);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String checkLinkProperties(LinkProperties linkProperties) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("hasProxy", linkProperties.getHttpProxy() != null);
            return join(linkedHashMap);
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
}
