package mbd.teacher.gurukuteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.activity.DetailStudentActivity;
import mbd.teacher.gurukuteacher.model.student.DataRequest;
import mbd.teacher.gurukuteacher.model.student.Student;
import mbd.teacher.gurukuteacher.model.transaction.Data;
import mbd.teacher.gurukuteacher.model.transaction.Transaction;

/**
 * Created by Naufal on 20/02/2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private Context context;
    private List<Data> listData;

    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public TransactionAdapter(Context context, List<Data> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Data data = listData.get(position);
        Student student = data.getStudent();
        Transaction transaction = data.getTransaction();

        int status = transaction.getStatus();

        String studentName = student.getFirstName() + " " + student.getLastName();
        String duration = String.valueOf(data.getDuration());

        setProfileImage(holder.profile_image, studentName);

        String information;

        if (status == 0) {
            information = "Menunggu pembayaran";
        } else {
            information = "Sudah dibayar";
        }

        holder.tvStudentName.setText(studentName);
        holder.tvInformation.setText(information);
        holder.tvDuration.setText(duration);
        holder.cvItemStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookData = new Gson().toJson(data);

                Intent i = new Intent(context, DetailStudentActivity.class);
                i.putExtra("from", "Trx");
                i.putExtra("bookData", bookData);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvItemStudent)
        CardView cvItemStudent;
        @BindView(R.id.profile_image)
        ImageView profile_image;
        @BindView(R.id.tvStudentName)
        TextView tvStudentName;
        @BindView(R.id.tvInformation)
        TextView tvInformation;
        @BindView(R.id.tvDuration)
        TextView tvDuration;

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
