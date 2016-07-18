package com.tongtong.ttmall.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.tongtong.ttmall.R;
import com.tongtong.ttmall.view.alertdialog.Effectstype;
import com.tongtong.ttmall.view.alertdialog.NiftyDialogBuilder;

/**
 * author:gaojingwei
 * date:2016/6/24
 * des:
 */
public class CustomDialogUtil {

    public static NiftyDialogBuilder dialogBuilder;

    /**
     * 显示自定义弹框
     * @param ctx
     * @param title 标题
     * @param resultString  显示的文本内容
     * @param confrimString 确定按钮显示的文本
     * @param confrimClickListener  点击确定按钮的监听
     * @param cancelString  取消按钮文本
     * @param cancelClickListener   点击取消按钮的监听
     */
    public static void showDialog(Context ctx, final String title, String resultString, String confrimString, View.OnClickListener confrimClickListener, String cancelString,View.OnClickListener cancelClickListener) {
        Effectstype effect = Effectstype.Fadein;
        dialogBuilder = NiftyDialogBuilder.getInstance(ctx);
        dialogBuilder.mLinearLayoutView.setBackgroundResource(R.drawable.shape_photo_pop_btn_normal);
        dialogBuilder.withTitle(title)
                .withTitleColor(Color.BLACK)
                .withMessage(resultString)
                .withMessageColor(Color.BLACK)
                .withEffect(effect)
                .withButton1Text(confrimString)
                .withButton1Drawable(R.drawable.selector_pop_cancel_btn_bg)
                .setButton1Click(confrimClickListener)
                .withButton2Text(cancelString)
                .withButton2Drawable(R.drawable.dialog_button_background)
                .setButton2Click(cancelClickListener)
                .show();
    }

}
