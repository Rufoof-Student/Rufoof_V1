package dev.Program.Backend.BusinessLayer.Process;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

import dev.Program.DTOs.Relax;
import dev.Program.DTOs.Exceptions.UserException;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import java.util.*;

public class ProcessController {
    private static List<ProcessObj> processes = new ArrayList<>();
    private static Map<String, String> processHistory = new HashMap<>();
    public static final int GWL_EXSTYLE = -20;
    public static final int WS_EX_TOOLWINDOW = 0x00000080;

    public interface MyUser32 extends User32 {
        MyUser32 INSTANCE = (MyUser32) Native.load("user32", MyUser32.class, W32APIOptions.DEFAULT_OPTIONS);

        boolean IsWindowVisible(WinDef.HWND hWnd);

        int GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);

        WinDef.HWND GetWindow(WinDef.HWND hWnd, int uCmd);

        int GetWindowLong(WinDef.HWND hWnd, int nIndex);
    }

    public static void initProcesses() {
        processes = new ArrayList<>();
        User32.INSTANCE.EnumWindows(new User32.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer arg1) {
                boolean oneChomeIsClosed = false;
                char[] windowText = new char[512];
                User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText).trim();
                int style = MyUser32.INSTANCE.GetWindowLong(hWnd, GWL_EXSTYLE);
                boolean isToolWindow = (style & WS_EX_TOOLWINDOW) != 0;
                if (!wText.isEmpty() && User32.INSTANCE.IsWindowVisible(hWnd) && !isToolWindow) {
                    ProcessObj processToAdd = new ProcessObj();
                    IntByReference processId = new IntByReference();
                    User32.INSTANCE.GetWindowThreadProcessId(hWnd, processId);
                    // System.out.println("--------------------");
                    // System.out.println(processId);
                    processToAdd.setId(processId);
                    WinNT.HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(
                            Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ | Kernel32.PROCESS_TERMINATE,
                            false, processId.getValue());
                    char[] exePath = new char[1024];
                    Psapi.INSTANCE.GetModuleFileNameExW(processHandle, null, exePath, exePath.length);
                    String windowTitle = Native.toString(exePath);
                    // System.out.println("Window Title: " + windowTitle);
                    // Convert the char array to a string and trim any extra spaces
                    String processName = Native.toString(exePath).trim();
                    processToAdd.setExePath(processName);
                    System.out.println("Open Window: " + wText + "\nWith process name:" + processName);
                    WinDef.RECT rect = new WinDef.RECT();
                    User32.INSTANCE.GetWindowRect(hWnd, rect);
                    // System.out.println(
                    // "Window position: (" + rect.left + ", " + rect.top + ")" +
                    // rect.toRectangle().getWidth());
                    // System.out.println("Window size: " + (rect.right - rect.left) + " x " +
                    // (rect.bottom - rect.top));
                    processToAdd.setProcessTitle(wText);
                    // Close the process handle

                    // if (!oneChomeIsClosed && getExeWindowName(processName).equals("chrome.exe"))
                    // {
                    // boolean res = Kernel32.INSTANCE.TerminateProcess(processHandle, 0);
                    // if (!res)
                    // System.out.println("faild");
                    // else
                    // oneChomeIsClosed = true;
                    // }

                    String exeName = getExeWindowName(processName);
                    if (!processHistory.containsKey(exeName))
                        processHistory.put(exeName, processName);
                    processes.add(processToAdd);
                    Kernel32.INSTANCE.CloseHandle(processHandle);
                }
                return true; // Continue enumeration
            }
        }, null);
    }

    private static String getExeWindowName(String processName) {
        if (processName.length() > 0) {

            int index = processName.length() - 1;

            String toRet = "";
            char lastChar = processName.charAt(index);
            while (lastChar != '\\' || index < 0) {
                toRet = lastChar + toRet;
                index--;
                lastChar = processName.charAt(index);
            }
            System.out.println(toRet + "-");
            return toRet;
        }
        return "";
    }

    public static boolean isChromeOpened(String chromeEngineName) {
        initProcesses();
        for (ProcessObj process : processes) {

            if (process.getExePath().endsWith(chromeEngineName))
                return true;

        }
        return false;
    }

    public static void runChrome(String chromeEngineName) throws UserException {
        if (processHistory.containsKey(chromeEngineName)) {
            // Specify the path to your .exe file
            String exePath = processHistory.get(chromeEngineName);

            // Create the process builder
            ProcessBuilder processBuilder = new ProcessBuilder(exePath);

            try {
                // Start the process
                Process process = processBuilder.start();
                Relax.Relax(1000);
                // Wait for the process to complete (optional)
                // int exitCode = process.waitFor();
                // System.out.println("Process exited with code: " + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new UserException("you have to run google");
        }
    }

    public static void runChromeFor(long seconds) {
        // initProcesses();
        if (processHistory.containsKey("chrome.exe")) {
            // Specify the path to your .exe file
            String exePath = processHistory.get("chrome.exe");

            // Create the process builder
            ProcessBuilder processBuilder = new ProcessBuilder(exePath);

            try {
                // Start the process
                Process process = processBuilder.start();

                // Wait for the process to complete (optional)
                // int exitCode = process.waitFor();
                // System.out.println(process.pid());
                // Thread.currentThread().sleep(seconds);
                // if (process.isAlive()) {
                // process.destroy();
                // System.out.println("Process terminated.");
                // } else {
                // System.out.println("Process already finished.");
                // }

                // process.destroy();
                System.out.println("Process exited with code: ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        initProcesses();
        // removeBlankApp("Word", "WINWORD.EXE");
        // User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
        // @Override
        // public boolean callback(WinDef.HWND hwnd, Pointer arg1) {
        // char[] windowText = new char[512];
        // User32.INSTANCE.GetWindowText(hwnd, windowText, 512);
        // String wText = Native.toString(windowText);

        // // Filter by Adobe Reader or any PDF viewer
        // if (!wText.isEmpty()
        // && (wText.contains(".pdf") || wText.contains("Adobe Reader") ||
        // wText.contains("Edge"))) {
        // System.out.println("Window title: " + wText);
        // }
        // return true;
        // }
        // }, null);
    }

    public static List<ProcessObj> getRunningProccesses() {
        initProcesses();
        return processes;
    }

    public static void removeBlankApp(String blankAppName, String exeName) {

        User32.INSTANCE.EnumWindows(new User32.WNDENUMPROC() {
            @Override
            public boolean callback(WinDef.HWND hWnd, Pointer arg1) {

                char[] windowText = new char[512];
                User32.INSTANCE.GetWindowText(hWnd, windowText, 512);
                String wText = Native.toString(windowText).trim();
                int style = MyUser32.INSTANCE.GetWindowLong(hWnd, GWL_EXSTYLE);
                boolean isToolWindow = (style & WS_EX_TOOLWINDOW) != 0;
                if (!wText.isEmpty() && User32.INSTANCE.IsWindowVisible(hWnd) && !isToolWindow) {
                    IntByReference processId = new IntByReference();
                    User32.INSTANCE.GetWindowThreadProcessId(hWnd, processId);

                    WinNT.HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(
                            Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ | Kernel32.PROCESS_TERMINATE,
                            false, processId.getValue());
                    char[] exePath = new char[1024];
                    Psapi.INSTANCE.GetModuleFileNameExW(processHandle, null, exePath, exePath.length);
                    String windowTitle = Native.toString(exePath);
                    // Convert the char array to a string and trim any extra spaces
                    String processName = Native.toString(exePath).trim();
                    WinDef.RECT rect = new WinDef.RECT();
                    User32.INSTANCE.GetWindowRect(hWnd, rect);
                    // Close the process handle

                    if (getExeWindowName(processName.toLowerCase()).equals(exeName.toLowerCase())
                            && wText.equals(blankAppName)) {
                        Kernel32.INSTANCE.TerminateProcess(processHandle, 0);

                    }

                    Kernel32.INSTANCE.CloseHandle(processHandle);
                }
                return true; // Continue enumeration
            }
        }, null);
    }

}
