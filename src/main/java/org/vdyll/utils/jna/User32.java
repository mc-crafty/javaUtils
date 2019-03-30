//                   ::::::::
//         :+:      :+:    :+:
//    +++++++++++  +:+         +++++
//       +:+      +#+         +#  +#
//      +#+      +#+         +#
//     #+#      #+#     +#  +#  +#
//    ###       ########+   ####+

package org.vdyll.utils.jna;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.ATOM;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HMENU;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPVOID;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Utility class intended to only expose the JNA actions needed by the author
 * for consumers of this package
 *
 * @author MC_Crafty
 *
 */
public interface User32 extends StdCallLibrary {
    public final static int W_APP         = 0x8000;
    public final static int WS_EX_TOPMOST = 0x0008;

    public User32           INSTANCE      = Native.loadLibrary("user32", User32.class);

    int SendMessageA(HWND hWnd, int msg, int num1, int num2);

    HWND FindWindowA(String className, String windowName);

    ATOM RegisterClassEx(WNDCLASSEX lpwcx);

    HWND CreateWindowEx(int dwExStyle, String lpClassName, String lpWindowName, int dwStyle, int x, int y, int nWidth,
            int nHeight, HWND hWndParent, HMENU hMenu, HINSTANCE hInstance, LPVOID lpParam);

    public static int makeLParam(final int low, final int high) {
        return low | high << 16;
    }
}
