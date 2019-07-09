package congdev37.edu.uttedudemo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.admin.fragment.AccountManagerFragment;
import congdev37.edu.uttedudemo.admin.fragment.StatisticalManagerFragment;
import congdev37.edu.uttedudemo.admin.fragment.SubjectManagerFragment;
import congdev37.edu.uttedudemo.model.Student;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.fragment.ChangePasswordFragment;
import congdev37.edu.uttedudemo.student.fragment.HistoryFragment;
import congdev37.edu.uttedudemo.student.fragment.ProfileFragment;
import congdev37.edu.uttedudemo.student.fragment.SubjectFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_layout)
    ConstraintLayout contentLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.titleToolbar)
    TextView titleToolbar;

    TextView navUsername, navClass, tvClass,tvPermission;
    Dialog dialogAddSubject;
    String permission;
    public static String stdCode, stdName;
    List<Student> mData;
    MenuItem menuItemAdd;

    SOService mService;
    EditText etSubjectName,etSubjectCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    // lấy dữ liệu khi đăng nhập thành công
    private void initData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                permission = bundle.getString("permission");
                stdCode = bundle.getString("code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ánh xạ view
    private void initView() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            //ánh xạ nav header
            navUsername = (TextView) headerView.findViewById(R.id.tvStdName);
            navClass = (TextView) headerView.findViewById(R.id.tvStdClass);
            tvClass = (TextView) headerView.findViewById(R.id.tvClass);
            tvPermission = (TextView) headerView.findViewById(R.id.tvPermission);

            if (permission.equals("admin")) {
                titleToolbar.setText("Quản lý bài test");
                hideFunctionNotAdmin();
                initFragment(new SubjectFragment());
            } else {
                titleToolbar.setText("Làm bài test");
                hideFunctionNotStudent();
                initFragment(new SubjectFragment());
            }
            if (permission.equals("admin")) {
                tvPermission.setText("Quản trị viên:");
                tvClass.setVisibility(View.INVISIBLE);
            } else {
                loadContactStudent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menuItemAdd = menu.getItem(1);
        hideItemAdd();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Logout) {
            finish();
        }
        if (id == R.id.action_Add) {
            showDialogAddSubject(new Subject("",""));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showItemAdd() {
        menuItemAdd.setVisible(true);
    }

    public void hideItemAdd() {
        menuItemAdd.setVisible(false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navManagerSub) {
            titleToolbar.setText("Quản lý môn học");
            initFragment(new SubjectManagerFragment());
            showItemAdd();
            initFragment(new SubjectManagerFragment());
        } else if (id == R.id.navManagerTest) {
            titleToolbar.setText("Quản lý bài test");
            hideItemAdd();
            initFragment(new SubjectFragment());
        } else if (id == R.id.navManagerAccount) {
            titleToolbar.setText("Quản lý tài khoản");
            hideItemAdd();
            initFragment(new AccountManagerFragment());
        } else if (id == R.id.navStatistical) {
            titleToolbar.setText("Thống kê bài làm");
            hideItemAdd();
            initFragment(new StatisticalManagerFragment());
        } else if (id == R.id.navTest) {
            titleToolbar.setText("Làm bài Test");
            hideItemAdd();
            initFragment(new SubjectFragment());
        } else if (id == R.id.navProfile) {
            titleToolbar.setText("Thông tin cá nhân");
            hideItemAdd();
            initFragment(new ProfileFragment());
        } else if (id == R.id.navExercise) {
            titleToolbar.setText("Lịch sử bài làm");
            hideItemAdd();
            initFragment(new HistoryFragment());
        } else if (id == R.id.navChangePass) {
            titleToolbar.setText("Đổi mật khẩu");
            hideItemAdd();
            initFragment(new ChangePasswordFragment());
        } else if (id == R.id.navLogout) {
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void initFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, fragment);
        ft.commit();
    }

    // ẩn các chức năng không phải của sinh viên
    private void hideFunctionNotStudent() {
        Menu nav_Menu = navView.getMenu();
        nav_Menu.findItem(R.id.navManagerSub).setVisible(false);
        nav_Menu.findItem(R.id.navManagerTest).setVisible(false);
        nav_Menu.findItem(R.id.navStatistical).setVisible(false);
        nav_Menu.findItem(R.id.navManagerAccount).setVisible(false);
    }

    // ẩn các chức năng không phải của admin
    private void hideFunctionNotAdmin() {
        Menu nav_Menu = navView.getMenu();
        nav_Menu.findItem(R.id.navTest).setVisible(false);
        nav_Menu.findItem(R.id.navExercise).setVisible(false);
    }

    //lây thông tin sinh viên
    private void loadContactStudent() {
        try {
            mData = new ArrayList<>();
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("stdCode", stdCode);
            mService.getStudent(params).enqueue(new Callback<List<Student>>() {
                @Override
                public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().size(); i++) {
                            try {
                                Student item = response.body().get(i);
                                Student student = new Student();
                                student.setID(item.getID());
                                student.setStdName(item.getStdName());
                                student.setStdCode(item.getStdCode());
                                student.setBirthday(item.getBirthday());
                                student.setAddress(item.getAddress());
                                student.setPhoneNumber(item.getPhoneNumber());
                                student.setClass_(item.getClass_());
                                student.setGender(item.getGender());
                                mData.add(student);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        stdName = mData.get(0).getStdName();
                        navUsername.setText(stdName);
                        navClass.setText(mData.get(0).getClass_());
                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<List<Student>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialogAddSubject(final Subject subject) {
        try {
            dialogAddSubject = new Dialog(this, R.style.Theme_Dialog);
            dialogAddSubject.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogAddSubject.setContentView(R.layout.dialog_add_edit_subject);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialogAddSubject.setCancelable(true);
            dialogAddSubject.setCanceledOnTouchOutside(true);

            //ánh xạ
            Button btnNo = dialogAddSubject.findViewById(R.id.btnCancel);
            Button btnYes = dialogAddSubject.findViewById(R.id.btnSave);
            ImageView btnTitleClose = dialogAddSubject.findViewById(R.id.btnTitleClose);
            etSubjectName = dialogAddSubject.findViewById(R.id.etSubjectName);
            etSubjectCode = dialogAddSubject.findViewById(R.id.etSubjectCode);

            etSubjectName.setText(subject.getSubjectName());
            etSubjectCode.setText(subject.getSubjectCode());
            // Chọn có xóa đơn vị
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddSubject.dismiss();
                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subName = etSubjectName.getText().toString().trim();
                    String subCode = etSubjectCode.getText().toString().trim();

                    if (subName.equals("")) {
                        Toast.makeText(HomeActivity.this, "Tên môn học không được để trống", Toast.LENGTH_SHORT).show();
                        etSubjectName.setError("Không được để trống");

                    } else if (subCode.equals("")) {
                        Toast.makeText(HomeActivity.this, "Mã môn học không được để trống", Toast.LENGTH_SHORT).show();
                        etSubjectName.setError("Không được để trống");
                    }else {
                        if(subject.getSubjectCode().equals("")){
                            createSubject(subName,subCode);
                        }else {
                            updateSubject(subject.getID(),subName,subCode);
                        }
                    }
                }
            });
            // Chọn không để đóng dialog
            btnTitleClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialogAddSubject.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialogAddSubject.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //thêm mới môn học
    private void createSubject(String subName, String subCode) {
        try {
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("subjectName", subName);
            params.put("subjectCode", subCode);
            mService.createSubject(params).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
                            if (currentFragment != null && currentFragment instanceof SubjectManagerFragment) {
                                ((SubjectManagerFragment) currentFragment).loadAllSubject();
                                dialogAddSubject.dismiss();
                            }

                        } else if(response.body().getSuccess() == 2){
                            Toast.makeText(HomeActivity.this, "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(HomeActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        int statusCode = response.code();
                        if (statusCode == 404) {
                            Toast.makeText(HomeActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sửa môn học
    private void updateSubject(String ID, String subName, String subCode) {
        try {
            mService = ApiUtils.getSOService();
            Map<String, Object> params = new HashMap<>();
            params.put("ID", ID);
            params.put("subjectName", subName);
            params.put("subjectCode", subCode);
            mService.updateSubject(params).enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
                            if (currentFragment != null && currentFragment instanceof SubjectManagerFragment) {
                                ((SubjectManagerFragment) currentFragment).loadAllSubject();
                                dialogAddSubject.dismiss();
                            }

                        } else if(response.body().getSuccess() == 2){
                            Toast.makeText(HomeActivity.this, "Lỗi: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(HomeActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        int statusCode = response.code();
                        if (statusCode == 404) {
                            Toast.makeText(HomeActivity.this, "Lỗi : Không thể kết nối tới máy chủ ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
