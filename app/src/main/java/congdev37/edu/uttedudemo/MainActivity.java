package congdev37.edu.uttedudemo;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import congdev37.edu.uttedudemo.admin.fragment.ExerciseManagerFragment;
import congdev37.edu.uttedudemo.admin.fragment.SubjectManagerFragment;
import congdev37.edu.uttedudemo.model.Student;
import congdev37.edu.uttedudemo.service.ApiUtils;
import congdev37.edu.uttedudemo.service.SOService;
import congdev37.edu.uttedudemo.student.fragment.ChangePasswordFragment;
import congdev37.edu.uttedudemo.student.fragment.ExerciseFragment;
import congdev37.edu.uttedudemo.student.fragment.ProfileStudentFragment;
import congdev37.edu.uttedudemo.student.fragment.SubjectFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_layout)
    ConstraintLayout contentLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    TextView navUsername, navClass, tvClass;

    String permission;
    public static String stdCode, stdName;
    List<Student> mData;
    MenuItem menuItemAdd;

    SOService mService;
    @BindView(R.id.titleToolbar)
    TextView titleToolbar;


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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            permission = bundle.getString("permission");
            stdCode = bundle.getString("code");
        }
    }

    // ánh xạ view
    private void initView() {
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
        navUsername = (TextView) headerView.findViewById(R.id.tvStdName);
        navClass = (TextView) headerView.findViewById(R.id.tvStdClass);
        tvClass = (TextView) headerView.findViewById(R.id.tvClass);
        if (permission.equals("admin")) {
            titleToolbar.setText("Quản lý bài test");
            hideFunctionNotAdmin();
            initFragment(new SubjectFragment());
        } else {
            titleToolbar.setText("Ôn luyện");
            hideFunctionNotStudent();
            initFragment(new SubjectFragment());
        }
        if (permission.equals("admin")) {
            tvClass.setVisibility(View.INVISIBLE);
        } else {
            loadContactStudent();
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
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (id == R.id.action_Logout) {
            finish();
        }
        if (id == R.id.action_Add) {
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
            showItemAdd();
            initFragment(new SubjectManagerFragment());
        } else if (id == R.id.navManagerTest) {
            titleToolbar.setText("Quản lý bài test");
            hideItemAdd();
            initFragment(new SubjectFragment());
        } else if (id == R.id.navStatistical) {
            titleToolbar.setText("Thống kê");
            hideItemAdd();
            initFragment(new ExerciseManagerFragment());
        } else if (id == R.id.navTest) {
            titleToolbar.setText("Ôn luyện");
            hideItemAdd();
            initFragment(new SubjectFragment());
        } else if (id == R.id.navProfile) {
            titleToolbar.setText("Thông tin cá nhân");
            hideItemAdd();
            if (permission.equals("admin")) {
            } else {
                initFragment(new ProfileStudentFragment());
            }
        } else if (id == R.id.navExercise) {
            titleToolbar.setText("Bài làm");
            hideItemAdd();
            initFragment(new ExerciseFragment());
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
    }

    // ẩn các chức năng không phải của admin
    private void hideFunctionNotAdmin() {
        Menu nav_Menu = navView.getMenu();
        nav_Menu.findItem(R.id.navTest).setVisible(false);
        nav_Menu.findItem(R.id.navExercise).setVisible(false);
    }

    private void loadContactStudent() {
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
    }

    private void loadContactAdmin() {

    }
}
