/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xiaopan.android.app;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MessageDialogFragment extends DialogFragment {
    public static String FRAGMENT_TAG_MESSAGE_DIALOG = "FRAGMENT_TAG_MESSAGE_DIALOG";
    public static String DEFAULT_CONFIRM_BUTTON_NAME_CHINA = "确定";
    public static String DEFAULT_CONFIRM_BUTTON_NAME_OTHER = "Confirm";

    private Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog messageDialog = new AlertDialog.Builder(getActivity()).create();
        applyParams(messageDialog);
        return messageDialog;
    }

    @Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(builder.onDismissListener != null){
			builder.onDismissListener.onDismiss(dialog);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if(builder.onCancelListener != null){
			builder.onCancelListener.onCancel(dialog);
		}
	}

	/**
     * 设置Builder
     * @param builder Builder
     */
    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    /**
     * 应用参数
     * @param messageDialog 对话框
     */
    private void applyParams(AlertDialog messageDialog){
        if(builder == null) throw new IllegalArgumentException("builder 为null 你需要调用setBuilder()方法设置Builder");
        if(builder.message == null) throw new IllegalArgumentException("请调用Builder.setMessage()设置消息");

        messageDialog.setTitle(builder.title);
        messageDialog.setMessage(builder.message);
        messageDialog.setButton(AlertDialog.BUTTON_POSITIVE, builder.confirmButtonName!=null?builder.confirmButtonName:(Locale.CHINA.equals(Locale.getDefault())?DEFAULT_CONFIRM_BUTTON_NAME_CHINA:DEFAULT_CONFIRM_BUTTON_NAME_OTHER), builder.confirmButtonClickListener);
        messageDialog.setButton(AlertDialog.BUTTON_NEGATIVE, builder.cancelButtonName, builder.cancelButtonClickListener);
        messageDialog.setOnKeyListener(builder.onKeyListener);
        messageDialog.setOnShowListener(builder.onShowListener);
        setCancelable(builder.cancelable);
    }

    /**
     * 更新
     */
    public void update(){
        Dialog dialog = getDialog();
        if(dialog == null || !(dialog instanceof AlertDialog)) return;
        applyParams((AlertDialog) dialog);
    }

    /**
     * 显示一个消息对话框
     * @param fragmentManager Fragment管理器
     * @param builder 构建器
     */
    public static void show(FragmentManager fragmentManager, MessageDialogFragment.Builder builder){
        if(fragmentManager == null) return;

        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG_MESSAGE_DIALOG);
        boolean old = fragment != null && fragment instanceof MessageDialogFragment;
        MessageDialogFragment messageDialogFragment = old?(MessageDialogFragment) fragment:new MessageDialogFragment();
        messageDialogFragment.setBuilder(builder);
        if(old){
            messageDialogFragment.update();
        }else{
            messageDialogFragment.show(fragmentManager, FRAGMENT_TAG_MESSAGE_DIALOG);
        }
    }

    /**
     * 关闭消息对话框
     * @param fragmentManager Fragment管理器
     */
    public static void close(FragmentManager fragmentManager){
        if(fragmentManager == null) return;

        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG_MESSAGE_DIALOG);
        if(fragment != null && fragment instanceof MessageDialogFragment){
            ((MessageDialogFragment) fragment).dismiss();
        }
    }

    public static class Builder{
        String title;
        String message;
        String confirmButtonName;
        String cancelButtonName;
        boolean cancelable = true;
        DialogInterface.OnClickListener confirmButtonClickListener;
        DialogInterface.OnClickListener cancelButtonClickListener;
        DialogInterface.OnShowListener onShowListener;
        DialogInterface.OnDismissListener onDismissListener;
        DialogInterface.OnCancelListener onCancelListener;
        DialogInterface.OnKeyListener onKeyListener;

        public Builder(String message) {
            this.message = message;
        }

        /**
         * 设置标题
         * @param title 标题
         * @return Builder
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置消息
         * @param message 消息
         * @return Builder
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置确定按钮名称
         * @param confirmButtonName 确定按钮名称
         * @return Builder
         */
        public Builder setConfirmButtonName(String confirmButtonName) {
            this.confirmButtonName = confirmButtonName;
            return this;
        }

        /**
         * 设置取消按钮名称
         * @param cancelButtonName 取消按钮名称
         * @return Builder
         */
        public Builder setCancelButtonName(String cancelButtonName) {
            this.cancelButtonName = cancelButtonName;
            return this;
        }

        /**
         * 设置是否可以取消
         * @param cancelable 是否可以取消
         * @return Builder
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * 设置确定按钮点击监听器
         * @param confirmButtonClickListener 确定按钮点击监听器
         * @return Builder
         */
        public Builder setConfirmButtonClickListener(DialogInterface.OnClickListener confirmButtonClickListener) {
            this.confirmButtonClickListener = confirmButtonClickListener;
            return this;
        }

        /**
         * 设置取消按钮点击监听器
         * @param cancelButtonClickListener 取消按钮点击监听器
         * @return Builder
         */
        public Builder setCancelButtonClickListener(DialogInterface.OnClickListener cancelButtonClickListener) {
            this.cancelButtonClickListener = cancelButtonClickListener;
            return this;
        }

        /**
         * 设置显示监听器
         * @param onShowListener 显示监听器
         */
        public Builder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        /**
         * 设置解除监听器
         * @param onDismissListener 解除监听器
         */
        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        /**
         * 设置取消监听器
         * @param onCancelListener 取消监听器
         */
        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        /**
         * 设置按键监听器
         * @param onKeyListener 按键监听器
         */
        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            this.onKeyListener = onKeyListener;
            return this;
        }

        /**
         * 显示
         * @param fragmentManager Fragment管理器
         */
        public void show(FragmentManager fragmentManager){
            MessageDialogFragment.show(fragmentManager, this);
        }
    }
}