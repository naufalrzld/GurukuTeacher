package mbd.teacher.gurukuteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.model.history.History;
import mbd.teacher.gurukuteacher.model.student.Student;

/**
 * Created by Naufal on 19/02/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<History> historyList;

    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        History history = historyList.get(position);
        Student student = history.getStudent();

        String namaGuru = student.getFirstName() + " " + student.getLastName();
        String statusMsg = "";
        int status = history.getStatus();
        int finish = history.getFinish();

        setProfileImage(holder.profileImage, namaGuru);

        if (status == 2) {
            statusMsg = "Ditolak";
            holder.iconStatus.setImageResource(R.drawable.ic_reject);
        } else {
            if (finish == 1) {
                statusMsg = "Selesai";
                holder.iconStatus.setImageResource(R.drawable.ic_finish);
            }
        }

        holder.tvNamaMurid.setText(namaGuru);
        holder.tvSatus.setText(statusMsg);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        ImageView profileImage;
        @BindView(R.id.tvNamaMurid)
        TextView tvNamaMurid;
        @BindView(R.id.tvStatus)
        TextView tvSatus;
        @BindView(R.id.icon_status)
        ImageView iconStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setProfileImage(ImageView profileImage, String nama) {
        String letter = "A";

        if(nama != null && !nama.isEmpty()) {
            letter = nama.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();
        mDrawableBuilder = TextDrawable.builder().buildRound(letter, color);
        profileImage.setImageDrawable(mDrawableBuilder);
    }
}
