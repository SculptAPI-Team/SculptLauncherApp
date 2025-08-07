package com.mojang.minecraftpe;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.Settings;
import com.mojang.minecraftpe.hardwareinfo.CPUCluster;
import com.mojang.minecraftpe.hardwareinfo.CPUTopologyInfo;
import com.mojang.minecraftpe.platforms.Platform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/HardwareInformation.class */
public class HardwareInformation {
    private static final CPUInfo cpuInfo = getCPUInfo();
    private final ApplicationInfo appInfo;
    private final Context context;
    private final PackageManager packageManager;

    /* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/HardwareInformation$CPUInfo.class */
    public static class CPUInfo {
        private final Map<String, String> cpuLines;
        private final int numberCPUCores;

        public CPUInfo(Map<String, String> map, int i) {
            this.cpuLines = map;
            this.numberCPUCores = i;
        }

        String getCPULine(String str) {
            return this.cpuLines.containsKey(str) ? this.cpuLines.get(str) : "";
        }

        int getNumberCPUCores() {
            return this.numberCPUCores;
        }
    }

    public HardwareInformation(Context context) {
        this.packageManager = context.getPackageManager();
        this.appInfo = context.getApplicationInfo();
        this.context = context;
    }

    private boolean appInstalled(String str) {
        try {
            this.packageManager.getPackageInfo(str, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRootA() {
        String str = Build.TAGS;
        return str != null && str.contains("test-keys");
    }

    private boolean checkRootB() {
        for (int i = 0; i < 10; i++) {
            if (new File(new String[]{"/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/system/app/Superuser.apk", "/data/local/su", "/su/bin/su"}[i]).exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRootC() {
        for (int i = 0; i < 2; i++) {
            if (appInstalled(new String[]{"eu.chainfire.supersu", "eu.chainfire.supersu.pro"}[i])) {
                return true;
            }
        }
        return false;
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getCPUFeatures() {
        return cpuInfo.getCPULine("Features");
    }

    public static CPUInfo getCPUInfo() {
        HashMap map = new HashMap();
        int i = 0;
        if (new File("/proc/cpuinfo").exists()) {
            int i2 = 0;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                Pattern patternCompile = Pattern.compile("([\\w\\ ]*)\\s*:\\s([^\\n]*)");
                i = 0;
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    Matcher matcher = patternCompile.matcher(line);
                    if (matcher.find() && matcher.groupCount() == 2) {
                        if (!map.containsKey(matcher.group(1))) {
                            map.put(matcher.group(1), matcher.group(2));
                        }
                        if (matcher.group(1).contentEquals("processor")) {
                            i++;
                        }
                    }
                }
                i2 = i;
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                i = i2;
            }
        }
        return new CPUInfo(map, i);
    }

    public static String getCPUName() {
        CPUInfo cPUInfo = cpuInfo;
        String cPULine = cPUInfo.getCPULine("model name");
        if (cPULine.isEmpty()) {
            cPULine = cPUInfo.getCPULine("Hardware");
        }
        return cPULine;
    }

    public static String getCPUType() {
        return Platform.createPlatform(false).getABIS();
    }

    public static String getDeviceModelName() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        return str2.startsWith(str) ? str2.toUpperCase(Locale.ENGLISH) : str.toUpperCase(Locale.ENGLISH) + " " + str2;
    }

    public static String getLocale() {
        return Locale.getDefault().toString();
    }

    public static int getNumClusters() {
        return CPUTopologyInfo.getInstance().getCPUClusterCount();
    }

    public static int getNumCores() {
        int cPUCount = CPUTopologyInfo.getInstance().getCPUCount();
        int numberCPUCores = cPUCount;
        if (cPUCount == 0) {
            numberCPUCores = cpuInfo.getNumberCPUCores();
        }
        return numberCPUCores;
    }

    public static int getPerformanceCoreCount() {
        CPUCluster[] clusterArray = CPUTopologyInfo.getInstance().getClusterArray();
        int numCores = getNumCores();
        if (clusterArray.length == 0) {
            return numCores > 2 ? numCores >> 1 : 1;
        }
        if (clusterArray.length == 1) {
            return numCores;
        }
        CPUCluster cPUCluster = clusterArray[0];
        long maxFreq = cPUCluster.getMaxFreq();
        for (int i = 1; i < clusterArray.length; i++) {
            if (clusterArray[i].getMaxFreq() < maxFreq) {
                cPUCluster = clusterArray[i];
            }
        }
        return numCores - cPUCluster.getClusterCoreCount();
    }

    public static String getSerialNumber() {
        return Build.SERIAL;
    }

    public static String getSoCName() {
        String cPULine = Build.VERSION.SDK_INT >= 31 ? Build.SOC_MODEL.equals("unknown") ? "" : Build.SOC_MODEL : cpuInfo.getCPULine("Hardware");
        String cPULine2 = cPULine;
        if (cPULine.isEmpty()) {
            cPULine2 = cpuInfo.getCPULine("model name");
        }
        return cPULine2;
    }

    public String getAndroidVersion() {
        return ((MainActivity) this.context).isChromebook() ? "ChromeOS " + Build.VERSION.RELEASE : "Android " + Build.VERSION.RELEASE;
    }

    public String getInstallerPackageName() {
        PackageManager packageManager = this.packageManager;
        return (packageManager == null || this.appInfo == null) ? "" : packageManager.getInstallerPackageName(this.context.getPackageName());
    }

    public boolean getIsRooted() {
        return checkRootA() || checkRootB() || checkRootC();
    }

    public String getSecureId() {
        return Settings.Secure.getString(this.context.getContentResolver(), "android_id");
    }

    public int getSignaturesHashCode() {
        Exception e;
        int iHashCode;
        try {
            iHashCode = 0;
            for (Signature signature : this.packageManager.getPackageInfo(this.context.getPackageName(), 64).signatures) {
                try {
                    iHashCode ^= signature.hashCode();
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    return iHashCode;
                }
            }
        } catch (Exception e3) {
            e = e3;
            iHashCode = 0;
        }
        return iHashCode;
    }
}
