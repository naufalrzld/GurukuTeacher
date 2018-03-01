package mbd.teacher.gurukuteacher.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.model.teacher.Category;

/**
 * Created by Naufal on 01/03/2018.
 */

public class CategoryAdapter extends AbstractItem<CategoryAdapter, CategoryAdapter.ViewHolder> {
    List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);

        addToSpinner(holder.spnCategory);
        holder.spnCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String name) {
                Log.d("category", String.valueOf(categories.get(position).getCategoryID()));
            }
        });
    }

    @Override
    public void unbindView(@NonNull ViewHolder holder) {
        super.unbindView(holder);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_category_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.field_category;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.spnCategory)
        public MaterialSpinner spnCategory;
        @BindView(R.id.btnDeleteField)
        public ImageButton btnDeleteField;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void addToSpinner(MaterialSpinner spnCategory) {
        List<String> categoryName = new ArrayList<>();

        if (!categories.isEmpty()) {
            for (Category category : categories) {
                categoryName.add(category.getCategoryName());
            }
        }

        spnCategory.setItems(categoryName);
    }
}
