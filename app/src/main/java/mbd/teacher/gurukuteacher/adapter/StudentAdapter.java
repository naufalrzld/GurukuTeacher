package mbd.teacher.gurukuteacher.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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
import mbd.teacher.gurukuteacher.model.student.DataRequest;
import mbd.teacher.gurukuteacher.model.student.Student;

/**
 * Created by Naufal on 20/02/2018.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private Context context;
    private List<DataRequest> dataRequestList;

    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public StudentAdapter(Context context, List<DataRequest> dataRequestList) {
        this.context = context;
        this.dataRequestList = dataRequestList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataRequest dataRequest = dataRequestList.get(position);
        Student student = dataRequest.getStudent();

        int status = dataRequest.getStatus();

        String studentName = student.getFirstName() + " " + student.getLastName();
        String duration = String.valueOf(dataRequest.getDuration());

        setProfileImage(holder.profile_image, studentName);

        String information;

        if (status == 0) {
            information = "Menunggu konfirmasi anda";
        } else {
            information = "Sudah dikonfirmasi";
        }

        holder.tvStudentName.setText(studentName);
        holder.tvInformation.setText(information);
        holder.tvDuration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return 0;
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
