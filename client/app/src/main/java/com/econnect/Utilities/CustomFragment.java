package com.econnect.Utilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class CustomFragment<T extends ViewBinding> extends Fragment {
    protected T binding;
    private final Class<T> _tClass;

    public CustomFragment(Class<T> tClass) {
        this._tClass = tClass;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using Java reflection

        // Get method "T.inflate(LayoutInflater inflater)"
        Method m;
        try {
            m = _tClass.getMethod("inflate", LayoutInflater.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Class" + _tClass.getName() + " has no method called 'inflate'");
        }

        // Call method "binding = T.inflate(inflate)"
        try {
            @SuppressWarnings("unchecked")
            T tmp = (T) m.invoke(null, inflater);
            binding = tmp;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error while inflating view " + _tClass.getName());
        }

        // Return view as usual
        assert binding != null;
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListeners();
    }

    protected abstract void addListeners();
}
