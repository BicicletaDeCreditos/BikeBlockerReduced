package bikeblocker.bikeblocker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.R;
/**
 * Created by atillagallio on 10/27/2015.
 */
public class AppsComparator implements Comparator<ApplicationInfo> {

    private final PackageManager mPackageManager;

    public AppsComparator(PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    @Override
    public int compare(ApplicationInfo app1, ApplicationInfo app2) {

            return app1.loadLabel(mPackageManager).toString().compareTo(app2.loadLabel(mPackageManager).toString());
    }
}