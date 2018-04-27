package com.example.sam.convert101;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public abstract class ConvertUnitsBase extends AppCompatActivity {

    int stringarray_units;
    int string_timedefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convert_units_base);


    }

    public int getIndex(Spinner spinner, String defaultPos) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(defaultPos)) {
                return i;
            }
        }
        return 0;
    }

    public void updateOutputValues(double[] out, double in, double[][] mat, int unit){
        for( int i = 0; i < mat.length; i++ ){
            out[i]=in*mat[unit][i];
        }
    }

    public void fillMatrix (double[][] mat, double[] fac) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                int chk = j-i;
                if (chk==0)
                    mat[i][j] = 1.0;
                else if (chk==1)
                    mat[i][j] = 1./fac[j-1];
                else if (chk==2)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]);
                else if (chk==3)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]);
                else if (chk==4)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]*fac[j-4]);
                else if (chk==5)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]*fac[j-4]*fac[j-5]);
                else if (chk==6)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]*fac[j-4]*fac[j-5]*fac[j-6]);
                else if (chk==7)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]*fac[j-4]*fac[j-5]*fac[j-6]*fac[j-7]);
                else if (chk==8)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]*fac[j-4]*fac[j-5]*fac[j-6]*fac[j-7]*fac[j-8]);
                else if (chk==9)
                    mat[i][j] = 1./(fac[j-1]*fac[j-2]*fac[j-3]*fac[j-4]*fac[j-5]*fac[j-6]*fac[j-7]*fac[j-8]*fac[j-9]);
                else if (chk==-1)
                    mat[i][j] = fac[i-1];
                else if (chk==-2)
                    mat[i][j] = fac[i-1]*fac[i-2];
                else if (chk==-3)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3];
                else if (chk==-4)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3]*fac[i-4];
                else if (chk==-5)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3]*fac[i-4]*fac[i-5];
                else if (chk==-6)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3]*fac[i-4]*fac[i-5]*fac[i-6];
                else if (chk==-7)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3]*fac[i-4]*fac[i-5]*fac[i-6]*fac[i-7];
                else if (chk==-8)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3]*fac[i-4]*fac[i-5]*fac[i-6]*fac[i-7]*fac[i-8];
                else if (chk==-9)
                    mat[i][j] = fac[i-1]*fac[i-2]*fac[i-3]*fac[i-4]*fac[i-5]*fac[i-6]*fac[i-7]*fac[i-8]*fac[i-9];
                else
                    mat[i][j] = 0.0;
            }
        }
    }



    public double[] resetOutputValues(double[] out, int dim){
        out = new double[dim];
        for(int i = 0; i<dim; i++) {
            out[i] = 0.0;
        }
        return out;
    }

    public void updateUnitData(String[] unitNames, UnitData[] data, UsersAdapter adapter, double[] out){
        for (int i = 0; i<unitNames.length; i++) {
            data[i] = new UnitData(unitNames[i], out[i]);
            adapter.addAll(data[i]);
        }
    }


}
