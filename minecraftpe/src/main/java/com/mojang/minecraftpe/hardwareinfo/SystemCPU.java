package com.mojang.minecraftpe.hardwareinfo;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/hardwareinfo/SystemCPU.class */
public class SystemCPU implements Comparable<SystemCPU> {
    protected static final String SYSTEM_CPU_PATH = "/sys/devices/system/cpu";
    private final BitSet CPU_BIT_MASK;
    private final int CPU_ID;
    private final String PATH;
    private BitSet siblingCoresMask;
    private long cpuMinFreq = 0;
    private long cpuMaxFreq = 0;

    SystemCPU(int i) {
        this.CPU_ID = i;
        BitSet bitSet = new BitSet();
        this.CPU_BIT_MASK = bitSet;
        bitSet.set(i);
        this.PATH = "/sys/devices/system/cpu/cpu" + i;
    }

    private File getSystemCPUFile() {
        File file = new File(this.PATH);
        if (!file.exists()) {
            Log.v("MCPE", "cpu" + this.CPU_ID + " directory doesn't exist: " + this.PATH);
            return null;
        }
        if (file.canRead()) {
            return file;
        }
        Log.v("MCPE", "Cannot read directory: " + this.PATH);
        return null;
    }

    private BitSet retrieveSiblingsMask() {
        File file;
        String str = this.PATH + "/topology";
        int i = 0;
        while (true) {
            if (i >= 2) {
                file = null;
                break;
            }
            file = new File(str + new String[]{"/core_siblings", "/package_cpus"}[i]);
            if (file.exists() && file.canRead()) {
                break;
            }
            i++;
        }
        if (file == null) {
            Log.v("MCPE", "Cannot read file: " + file.getAbsolutePath());
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            try {
                String[] strArrSplit = bufferedReader.readLine().split(",");
                BitSet bitSet = new BitSet(strArrSplit.length * 32);
                for (int i2 = 0; i2 < strArrSplit.length; i2 += 2) {
                    long j = Long.parseLong(strArrSplit[i2].trim().toUpperCase(), 16);
                    int i3 = i2 + 1;
                    bitSet.or(BitSet.valueOf(new long[]{((i3 < strArrSplit.length ? Long.parseLong(strArrSplit[i3].trim().toUpperCase(), 16) : 0L) << 32) | j}));
                }
                bufferedReader.close();
                return bitSet;
            } finally {
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private long tryReadFreq(String str, String str2) {
        File file = new File(this.PATH + "/cpufreq/" + str + "_" + str2 + "_freq");
        if (!file.exists() || !file.canRead()) {
            return 0L;
        }
        try {
            Scanner scanner = new Scanner(file);
            try {
                long jNextInt = scanner.nextInt();
                scanner.close();
                return jNextInt;
            } finally {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(SystemCPU systemCPU) {
        return Integer.compare(this.CPU_ID, systemCPU.CPU_ID);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (((SystemCPU) obj).CPU_ID != this.CPU_ID) {
            z = false;
        }
        return z;
    }

    boolean exists() {
        return getSystemCPUFile() != null;
    }

    public int getCPUId() {
        return this.CPU_ID;
    }

    public BitSet getCPUMask() {
        return (BitSet) this.CPU_BIT_MASK.clone();
    }

    public long getMaxFrequencyHz() {
        return this.cpuMaxFreq;
    }

    public long getMinFrequencyHz() {
        return this.cpuMinFreq;
    }

    public Set<SystemCPU> getSiblingCPUs() {
        List<SystemCPU> cpus = CPUTopologyInfo.getInstance().getCPUS();
        TreeSet treeSet = new TreeSet();
        BitSet siblingsMask = getSiblingsMask();
        if (siblingsMask != null && siblingsMask.length() != 0) {
            int i = 0;
            while (true) {
                int iNextSetBit = siblingsMask.nextSetBit(i);
                if (iNextSetBit < 0) {
                    break;
                }
                treeSet.add(cpus.get(iNextSetBit));
                i = iNextSetBit + 1;
            }
        }
        return treeSet;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public String getSiblingString() {
        /*
            Method dump skipped, instructions count: 214
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mojang.minecraftpe.hardwareinfo.SystemCPU.getSiblingString():java.lang.String");
    }

    public BitSet getSiblingsMask() {
        if (this.siblingCoresMask == null) {
            this.siblingCoresMask = retrieveSiblingsMask();
        }
        return this.siblingCoresMask;
    }

    public int hashCode() {
        return this.CPU_ID;
    }

    public String toString() {
        return this.PATH;
    }

    void updateCPUFreq() {
        long jTryReadFreq = tryReadFreq("cpuinfo", "min");
        this.cpuMinFreq = jTryReadFreq;
        if (jTryReadFreq == 0) {
            this.cpuMinFreq = tryReadFreq("scaling", "min");
        }
        long jTryReadFreq2 = tryReadFreq("cpuinfo", "max");
        this.cpuMaxFreq = jTryReadFreq2;
        if (jTryReadFreq2 == 0) {
            this.cpuMaxFreq = tryReadFreq("scaling", "max");
        }
    }
}
