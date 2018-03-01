package mbd.teacher.gurukuteacher.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter.listeners.EventHook;
import com.mikepenz.fastadapter.select.SelectExtension;
import com.mikepenz.fastadapter.utils.EventHookUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbd.teacher.gurukuteacher.R;
import mbd.teacher.gurukuteacher.adapter.CategoryAdapter;
import mbd.teacher.gurukuteacher.model.APIErrorModel;
import mbd.teacher.gurukuteacher.model.teacher.Category;
import mbd.teacher.gurukuteacher.model.teacher.CategoryResponse;
import mbd.teacher.gurukuteacher.services.RetrofitServices;
import mbd.teacher.gurukuteacher.utils.APIErrorUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.ilFirstName)
    TextInputLayout ilFirstName;
    @BindView(R.id.ilLastName)
    TextInputLayout ilLastName;
    @BindView(R.id.ilUsername)
    TextInputLayout ilUsername;
    @BindView(R.id.ilEmail)
    TextInputLayout ilEmail;
    @BindView(R.id.ilNoTlp)
    TextInputLayout ilNoTlp;
    @BindView(R.id.ilPrice)
    TextInputLayout ilPrice;
    @BindView(R.id.ilPassword)
    TextInputLayout ilPassword;
    @BindView(R.id.ilCPassword)
    TextInputLayout ilCPassword;

    @BindView(R.id.etFirstName)
    TextInputEditText etFirstName;
    @BindView(R.id.etLastName)
    TextInputEditText etLastName;
    @BindView(R.id.etUsername)
    TextInputEditText etUsername;
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @BindView(R.id.etNoTlp)
    TextInputEditText etNoTlp;
    @BindView(R.id.etPrice)
    TextInputEditText etPrice;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.etCPassword)
    TextInputEditText etCPassword;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;

    @BindView(R.id.btnDaftar)
    Button btnDaftar;
    @BindView(R.id.btnAddNewCategory)
    Button btnAddNewCategory;

    @BindView(R.id.spnCategory)
    MaterialSpinner spnCategory;

    private List<Category> categories = new ArrayList<>();
    private FastAdapter<CategoryAdapter> mCategoryAdapter;
    private ItemAdapter<CategoryAdapter> mCategoryItemAdapter;
    private SelectExtension<CategoryAdapter> selectExtension;

    private ProgressDialog registerLoading;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerLoading = new ProgressDialog(this);
        registerLoading.setMessage("Loading . . .");
        registerLoading.setCancelable(false);

        spnCategory.setText("Memuat . . .");

        mCategoryItemAdapter = new ItemAdapter<>();

        mCategoryAdapter = FastAdapter.with(Arrays.asList(mCategoryItemAdapter));
        mCategoryAdapter.withSelectable(true);
        mCategoryAdapter.withMultiSelect(true);
        mCategoryAdapter.withSelectOnLongClick(false);
        selectExtension = mCategoryAdapter.getExtension(SelectExtension.class);

        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setAdapter(mCategoryAdapter);

        btnAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastVisiblePosition;
                lastVisiblePosition = ((LinearLayoutManager) rvCategory.getLayoutManager()).findLastVisibleItemPosition();
                mCategoryItemAdapter.add(lastVisiblePosition + 1, new CategoryAdapter(categories));
                rvCategory.scrollToPosition(lastVisiblePosition+1);
            }
        });

        mCategoryAdapter.withEventHook(new ClickEventHook<CategoryAdapter>() {
            @Nullable
            @Override
            public View onBind(RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof CategoryAdapter.ViewHolder) {
                    return ((CategoryAdapter.ViewHolder) viewHolder).btnDeleteField;
                }
                return null;
            }

            @Override
            public void onClick(View v, int position, FastAdapter<CategoryAdapter> fastAdapter, CategoryAdapter item) {
                selectExtension.select(position);
                selectExtension.deleteAllSelectedItems();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    String fName = etFirstName.getText().toString();
                    String lName = etLastName.getText().toString();
                    String username = etUsername.getText().toString();
                    String email = etEmail.getText().toString();
                    String noTpl = etNoTlp.getText().toString();
                    String tarif = etPrice.getText().toString();
                    String password = etPassword.getText().toString();

                    int index = spnCategory.getSelectedIndex();
                    int categoryID = categories.get(index).getCategoryID();

                    try {
                        JSONObject params = new JSONObject();
                        params.put("username", username);
                        params.put("firstName", fName);
                        params.put("lastName", lName);
                        params.put("email", email);
                        params.put("no_tlp", noTpl);
                        params.put("categoryID", categoryID);
                        params.put("price", tarif);
                        params.put("password", password);

                        register(params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        getCategory();
    }

    private boolean isInputValid() {
        ilFirstName.setErrorEnabled(false);
        ilLastName.setErrorEnabled(false);
        ilUsername.setErrorEnabled(false);
        ilEmail.setErrorEnabled(false);
        ilNoTlp.setErrorEnabled(false);
        ilPrice.setErrorEnabled(false);
        ilPassword.setErrorEnabled(false);
        ilCPassword.setErrorEnabled(false);

        if (TextUtils.isEmpty(etFirstName.getText().toString()) ||
                TextUtils.isEmpty(etLastName.getText().toString()) ||
                TextUtils.isEmpty(etUsername.getText().toString()) ||
                TextUtils.isEmpty(etEmail.getText().toString()) ||
                TextUtils.isEmpty(etNoTlp.getText().toString()) ||
                TextUtils.isEmpty(etPrice.getText().toString()) ||
                TextUtils.isEmpty(etPassword.getText().toString()) ||
                TextUtils.isEmpty(etCPassword.getText().toString())) {
            if (TextUtils.isEmpty(etFirstName.getText().toString())) {
                ilFirstName.setErrorEnabled(true);
                ilFirstName.setError("Silahkan masukkan nama depan anda");
            }

            if (TextUtils.isEmpty(etLastName.getText().toString())) {
                ilLastName.setErrorEnabled(true);
                ilLastName.setError("Silahkan masukkan nama bekalang anda");
            }

            if (TextUtils.isEmpty(etUsername.getText().toString())) {
                ilUsername.setErrorEnabled(true);
                ilUsername.setError("Silahkan masukkan username anda");
            }

            if (TextUtils.isEmpty(etEmail.getText().toString())) {
                ilEmail.setErrorEnabled(true);
                ilEmail.setError("Silahkan masukkan alamat email anda");
            }

            if (TextUtils.isEmpty(etNoTlp.getText().toString())) {
                ilNoTlp.setErrorEnabled(true);
                ilNoTlp.setError("Silahkan masukkan nomor telepon anda");
            }

            if (TextUtils.isEmpty(etPrice.getText().toString())) {
                ilPrice.setErrorEnabled(true);
                ilPrice.setError("Silahkan masukkan tarif per jam anda");
            }

            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                ilPassword.setErrorEnabled(true);
                ilPassword.setError("Silahkan masukkan password anda");
            }

            if (TextUtils.isEmpty(etCPassword.getText().toString())) {
                ilCPassword.setErrorEnabled(true);
                ilCPassword.setError("Silahkan ulangi password anda");
            }

            return false;
        } else {
            if (etPassword.getText().toString().length() < 6) {
                ilPassword.setErrorEnabled(true);
                ilPassword.setError("Minimal password 6 karakter");

                return false;
            }

            if (etCPassword.getText().toString().length() < 6) {
                ilCPassword.setErrorEnabled(true);
                ilCPassword.setError("Minimal password 6 karakter");

                return false;
            } else {
                if (!etCPassword.getText().toString().equals(etPassword.getText().toString())) {
                    ilCPassword.setErrorEnabled(true);
                    ilCPassword.setError("Password tidak cocok");

                    return false;
                }
            }
        }

        return true;
    }

    private void setToSpinner(List<Category> categories, MaterialSpinner spnCategory) {
        List<String> categoryName = new ArrayList<>();

        if (!categories.isEmpty()) {
            for (Category category : categories) {
                categoryName.add(category.getCategoryName());
            }
        }

        spnCategory.setItems(categoryName);
    }

    private void getCategory() {
        Call<CategoryResponse> call = RetrofitServices.sendTeacherRequest().APIGetAllCategory();
        if (call != null) {
            call.enqueue(new Callback<CategoryResponse>() {
                @Override
                public void onResponse(@NonNull Call<CategoryResponse> call, @NonNull Response<CategoryResponse> response) {
                    if (response.isSuccessful()) {
                        spnCategory.setText("");
                        int size = response.body().getData().size();
                        for (int i=0; i<size; i++) {
                            categories.add(response.body().getData().get(i));
                        }

                        Collections.sort(categories, new Comparator<Category>() {
                            @Override
                            public int compare(Category s1, Category s2) {
                                return s1.getCategoryName().compareTo(s2.getCategoryName());
                            }
                        });

                        setToSpinner(categories, spnCategory);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    private void register(JSONObject param) {
        registerLoading.show();
        Call<String> call = RetrofitServices.sendTeacherRequest().APIRegister(param);
        if (call != null) {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    registerLoading.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            JSONObject result = new JSONObject(response.body());

                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            APIErrorModel error = APIErrorUtils.parserError(response);
                            String message = error.getMessage();
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Pesan")
                                    .setMessage(message)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    registerLoading.dismiss();
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    protected static class FieldCategoryView {
        @BindView(R.id.spnCategory)
        MaterialSpinner spnCategory;
        @BindView(R.id.btnDeleteField)
        ImageButton btnDeleteField;

        FieldCategoryView(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
