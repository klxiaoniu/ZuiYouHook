package com.xiaoniu.zuiyouhook;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("cn.xiaochuankeji.tieba")) {
            //XposedBridge.log("Loaded app: " + lpparam.packageName);
            Class clazz = XposedHelpers.findClass("com.qq.e.comm.managers.b", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(clazz, "d", new XC_MethodHook() {
                @Override
                //在被hook的方法执行之后执行这里的代码
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(false);
                }
            });

            clazz = XposedHelpers.findClass("com.bytedance.sdk.openadsdk.TTAdConfig", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(clazz, "getSdkInfo", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(null);
                }
            });

            clazz = XposedHelpers.findClass("com.qumeng.advlib.api.AiClkAdManager", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(clazz, "init", Context.class, String.class, new XC_MethodReplacement() {
                @Override
                //拦截执行
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return null;
                }
            });

            clazz = XposedHelpers.findClass("cn.xiaochuankeji.xcad.sdk.XcADSdk", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(clazz, "init", Application.class, String.class, new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return null;
                }
            });
        }
    }

}
