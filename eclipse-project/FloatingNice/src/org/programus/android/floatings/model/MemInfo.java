package org.programus.android.floatings.model;

import android.app.ActivityManager;
import android.content.Context;
import android.os.*;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemInfo {
    private static final String TAG = "MemInfo";
	public static ActivityManager.MemoryInfo getmem_INFO(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi;
    }
	public static long getmem_UNUSED(Context context) {
		long MEM_UNUSED;

		MEM_UNUSED = getmem_INFO(context).availMem / 1024;
		return MEM_UNUSED;
	}
	
	public static long getmem_TOLAL() {
		long mTotal;
		// ϵͳ�ڴ�
		String path = "/proc/meminfo";
		// �洢������
		String content = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path), 8);
			String line;
			if ((line = br.readLine()) != null) {
				// �ɼ��ڴ���Ϣ
				content = line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// beginIndex
		int begin = content.indexOf(':');
		// endIndex
		int end = content.indexOf('k');
		// �ɼ��������ڴ�
		content = content.substring(begin + 1, end).trim();
		// ת��ΪInt��
		mTotal = Integer.parseInt(content);
		return mTotal;
	}

    public static long getmem_CURRENT(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        List<ActivityManager.RunningAppProcessInfo> apps = am.getRunningAppProcesses();

        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (ActivityManager.RunningAppProcessInfo info : apps) {
            ids.add(info.pid);
            Log.v(TAG, "getmem_CURRENT, " + info.toString() + ", name = " + info.processName);
        }

        int[] processes = new int[] { apps.get(1).pid };
        Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(processes)[0];
        return memoryInfo.getTotalPrivateDirty();
    }
}
