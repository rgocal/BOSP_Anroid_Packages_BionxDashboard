package com.bionx.res.util;

import android.util.Log;

// convenience import for quick referencing of this method

public final class GPUProcessor {
    private static final String TAG = "GPUProcessor";

    private GPUProcessor() {
        // Cannot instantiate this class
        throw new AssertionError();
    }

    /* Run a system command with full redirection */
    public static GPUChildProcess startSysCmd(String[] cmdarray, String childStdin) {
        return new GPUChildProcess(cmdarray, childStdin);
    }

    public static GPUCommandResult runSysCmd(String[] cmdarray, String childStdin) {
        GPUChildProcess proc = startSysCmd(cmdarray, childStdin);
        proc.waitFinished();
        return proc.getResult();
    }

    public static GPUChildProcess startShellCommand(String cmd) {
        String[] cmdarray = new String[3];
        cmdarray[0] = "sh";
        cmdarray[1] = "-c";
        cmdarray[2] = cmd;
        return startSysCmd(cmdarray, null);
    }

    public static GPUCommandResult runShellCommand(String cmd) {
        GPUChildProcess proc = startShellCommand(cmd);
        proc.waitFinished();
        return proc.getResult();
    }

    public static GPUChildProcess startSuCommand(String cmd) {
        String[] cmdarray = new String[3];
        cmdarray[0] = "su";
        cmdarray[1] = "-c";
        cmdarray[2] = cmd;
        return startSysCmd(cmdarray, null);
    }

    public static GPUCommandResult runSuCommand(String cmd) {
        GPUChildProcess proc = startSuCommand(cmd);
        proc.waitFinished();
        return proc.getResult();
    }

    public static boolean canSU() {
        GPUCommandResult r = runShellCommand("id");
        StringBuilder out = new StringBuilder(0);
        out.append(r.getStdout());
        out.append(" ; ");
        out.append(r.getStderr());
        Log.d(TAG, "canSU() su[" + r.getExitValue() + "]: " + out);
        return r.success();
    }
}
