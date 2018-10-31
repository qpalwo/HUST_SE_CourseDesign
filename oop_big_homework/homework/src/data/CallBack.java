package data;

public interface CallBack<T> {
    void onSuccess(T data);

    void onFailed(String s);
}
