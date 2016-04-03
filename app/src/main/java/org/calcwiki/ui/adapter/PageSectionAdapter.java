package org.calcwiki.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.calcwiki.R;
import org.calcwiki.data.model.MobileViewModel;
import org.calcwiki.ui.util.HighLight;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PageSectionAdapter extends RecyclerArrayAdapter<MobileViewModel.Page.MobileviewEntity.SectionsEntity> {

    public PageSectionAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PageSectionViewHolder(parent);
    }

    public class PageSectionViewHolder extends BaseViewHolder<MobileViewModel.Page.MobileviewEntity.SectionsEntity> {

        LinearLayout wholeView;
        TextView lineTextView;
        WebView textWebView;
        int state = 0;

        public PageSectionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_page_section);
            wholeView = $(R.id.item_section);
            lineTextView = $(R.id.section_line);
            textWebView = $(R.id.section_text);
        }

        @Override
        public void setData(MobileViewModel.Page.MobileviewEntity.SectionsEntity data) {
            if (data.line != null && !data.line.equals("")) {
                lineTextView.setText(HighLight.highLightSectionLine(data.line, data.toclevel));
                if (!data.text.equals("")) {
                    textWebView.loadDataWithBaseURL("https://calcwiki.org/", data.text, "text/html", "UTF-8", null);
                    wholeView.setOnClickListener(new ExpandListener());
                    if (state == 0 || state == -1) {
                        textWebView.setVisibility(View.GONE);
                    } else if (state == 1) {
                        textWebView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                lineTextView.setVisibility(View.GONE);
                textWebView.loadDataWithBaseURL("https://calcwiki.org/", data.text, "text/html", "UTF-8", null);
                textWebView.setVisibility(View.VISIBLE);
            }
        }

        public class ExpandListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                state = 1;
                textWebView.setVisibility(View.VISIBLE);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = -1;
                        textWebView.setVisibility(View.GONE);
                        v.setOnClickListener(ExpandListener.this);
                    }
                });
            }
        }
    }

}
