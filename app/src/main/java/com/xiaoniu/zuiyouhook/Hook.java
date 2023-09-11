package com.xiaoniu.zuiyouhook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("cn.xiaochuankeji.tieba")) {
            hookAdSdk(lpparam);
            hookYoungDialog(lpparam);
            hookVideo(lpparam);
        }
    }

    // 去广告
    private void hookAdSdk(XC_LoadPackage.LoadPackageParam lpparam) {
        Method[] ms = XposedHelpers.findClass("cn.xiaochuankeji.hermes.HermesSDK", lpparam.classLoader).getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals("install")) {
                XposedBridge.hookMethod(m, XC_MethodReplacement.returnConstant(null));
                break;
            }
        }
    }

    // 去除青少年弹窗提示
    private void hookYoungDialog(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("cn.xiaochuankeji.tieba.ui.home.page.PageMainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                SharedPreferences sp = activity.getSharedPreferences("common", Context.MODE_PRIVATE);
                sp.edit().putLong("skey_show_young_mode_guide_time", System.currentTimeMillis()).apply();
            }
        });
    }

    private void hookVideo(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> clazz = XposedHelpers.findClass("cn.xiaochuankeji.tieba.background.data.ServerVideo", lpparam.classLoader);
        XposedBridge.hookAllConstructors(clazz, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object ob = param.thisObject;
                Object a = XposedHelpers.getObjectField(ob, "url");
                XposedHelpers.setObjectField(ob, "downloadUrl", a);
            }
        });
    }
}
