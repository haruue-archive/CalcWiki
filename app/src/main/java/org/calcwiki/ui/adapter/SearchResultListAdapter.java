package org.calcwiki.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.utils.JUtils;

import org.calcwiki.R;
import org.calcwiki.data.model.SearchModel;
import org.calcwiki.ui.util.SearchResultHighLight;
import org.calcwiki.util.Utils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class SearchResultListAdapter extends RecyclerArrayAdapter<SearchModel.Result.QueryEntity.SearchEntity> {

    public SearchResultListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchResultListViewHolder(parent);
    }

    class SearchResultListViewHolder extends BaseViewHolder<SearchModel.Result.QueryEntity.SearchEntity> {

        TextView titleTextView;
        TextView snippetTextView;
        LinearLayout wholeItem;

        public SearchResultListViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_search_result);
            titleTextView = $(R.id.textview_title_in_search_result);
            snippetTextView = $(R.id.textview_snippet_in_search_result);
            wholeItem = $(R.id.whole_item);
        }

        @Override
        public void setData(SearchModel.Result.QueryEntity.SearchEntity data) {
            titleTextView.setText(data.title);
            snippetTextView.setText(SearchResultHighLight.highLightByClass(data.snippet));
            if (getPosition() % 2 != 0) {
                JUtils.Log("AdapterPosition", getAdapterPosition() + "");
                JUtils.Log("LayoutPosition", getLayoutPosition() + "");
                wholeItem.setBackgroundColor(Utils.getApplication().getResources().getColor(R.color.gray_200));
            }
        }

    }
}
