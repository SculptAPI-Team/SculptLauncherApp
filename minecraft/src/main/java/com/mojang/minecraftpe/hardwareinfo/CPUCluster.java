package com.mojang.minecraftpe.hardwareinfo;

import java.util.BitSet;
import java.util.Set;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/hardwareinfo/CPUCluster.class */
public class CPUCluster implements Comparable<CPUCluster> {
    private BitSet bitmask = new BitSet();
    private Set<SystemCPU> clusterCPUs;
    int[] cpuIds;
    private long maxFreq;
    private long minFreq;
    String siblingsString;

    CPUCluster(String str, Set<SystemCPU> set) {
        this.minFreq = 2147483647L;
        this.maxFreq = -2147483648L;
        this.clusterCPUs = set;
        this.siblingsString = str;
        this.cpuIds = new int[set.size()];
        int i = 0;
        for (SystemCPU systemCPU : set) {
            this.cpuIds[i] = systemCPU.getCPUId();
            this.bitmask.or(systemCPU.getCPUMask());
            this.minFreq = Math.min(systemCPU.getMinFrequencyHz(), this.minFreq);
            this.maxFreq = Math.max(systemCPU.getMaxFrequencyHz(), this.maxFreq);
            i++;
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(CPUCluster cPUCluster) {
        BitSet bitSet = (BitSet) this.bitmask.clone();
        bitSet.xor(cPUCluster.bitmask);
        if (bitSet.isEmpty()) {
            return 0;
        }
        return bitSet.length() == cPUCluster.bitmask.length() ? -1 : 1;
    }

    public boolean contains(int i) {
        return this.clusterCPUs.contains(Integer.valueOf(i));
    }

    public SystemCPU[] getCPUArray() {
        Set<SystemCPU> set = this.clusterCPUs;
        return (SystemCPU[]) set.toArray(new SystemCPU[set.size()]);
    }

    public int[] getCPUIds() {
        return (int[]) this.cpuIds.clone();
    }

    public int getClusterCoreCount() {
        return this.clusterCPUs.size();
    }

    public long getMaxFreq() {
        return this.maxFreq;
    }

    public long getMinFreq() {
        return this.minFreq;
    }

    public String getSiblingsString() {
        return this.siblingsString;
    }

    public int hashCode() {
        return this.bitmask.hashCode();
    }
}
