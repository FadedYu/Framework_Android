package com.bonait.bnframework.common.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bonait.bnframework.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

/**
 * Created by LY on 2019/3/25.
 */
public class QMAutoDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {

    private Context mContext;
    private EditText mEditText;

    public QMAutoDialogBuilder(Context context) {
        super(context);
        mContext = context;
    }

    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = QMUIDisplayHelper.dp2px(mContext, 20);
        layout.setPadding(padding, padding, padding, padding);
        mEditText = new EditText(mContext);
        QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
        mEditText.setHint("输入框");
        LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
        editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(mContext, 15);
        mEditText.setLayoutParams(editTextLP);
        layout.addView(mEditText);
        TextView textView = new TextView(mContext);
        textView.setLineSpacing(QMUIDisplayHelper.dp2px(mContext, 4), 1.0f);
        textView.setText("观察聚焦输入框后，键盘升起降下时 dialog 的高度自适应变化。\n\n" +
                "QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目，" +
                "同时利用自身提供的丰富控件及兼容处理，让开发者能专注于业务需求而无需耗费精力在基础代码的设计上。" +
                "不管是新项目的创建，或是已有项目的维护，均可使开发效率和项目质量得到大幅度提升。");
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.app_color_description));
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(textView);
        return layout;
    }
}
