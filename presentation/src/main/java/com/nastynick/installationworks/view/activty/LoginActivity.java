package com.nastynick.installationworks.view.activty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nastynick.installationworks.R;
import com.nastynick.installationworks.databinding.ActivityLoginBinding;
import com.nastynick.installationworks.model.CredentialsModel;
import com.nastynick.installationworks.presenter.LoginPresenter;
import com.nastynick.installationworks.view.LoginView;
import com.reginald.editspinner.EditSpinner;

import java.util.List;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginView {
    @Inject
    protected LoginPresenter loginPresenter;

    ActivityLoginBinding binding;
    CredentialsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getAppComponent().inject(this);
        loginPresenter.setView(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initSpinner(binding.login);
        loginPresenter.setCredentials();
    }

    private void initSpinner(EditSpinner spinner) {
        spinner.setItemConverter(selectedItem -> {
            CredentialsModel credentials = (CredentialsModel) selectedItem;
            return credentials.login;
        });
        spinner.setOnItemClickListener((adapterView, view, position, id) -> {
            CredentialsModel credentials = adapter.getItem(position);
            renderCredentials(credentials);
        });
    }

    @Override
    public void renderCredentials(CredentialsModel credentials) {
        binding.setCredentials(credentials);
    }

    public void login(View v) {
        loginPresenter.saveCredentials();
    }

    @Override
    public String login() {
        return String.valueOf(binding.login.getText());
    }

    @Override
    public String password() {
        return String.valueOf(binding.password.getText());
    }

    @Override
    public void success() {
        toast(R.string.authorization_success);
        startActivity(InstallationWorkCaptureActivity.class);
        finish();
    }

    @Override
    public void fail(int mesId) {
        toast(mesId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void renderCredentialsList(List<CredentialsModel> data) {
        adapter = new CredentialsAdapter(data);
        binding.login.setAdapter(adapter);
    }

    private class CredentialsAdapter extends BaseAdapter {

        private List<CredentialsModel> data;

        CredentialsAdapter(List<CredentialsModel> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public CredentialsModel getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view;
            if (convertView == null) {
                view = View.inflate(LoginActivity.this, R.layout.v_spinner_item, null);
            } else view = convertView;
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position).login);
            return view;
        }
    }
}
