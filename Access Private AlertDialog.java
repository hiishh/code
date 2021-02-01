    private static AlertDialog createAlertDialog(Context context) {
        try {
            //alternative method alertdialog (by dise?)
            Constructor declaredConstructor = AlertDialog.class.getDeclaredConstructor(Context.class);
            declaredConstructor.setAccessible(true);
            //allowing access to alertdialog private class (as alertdialog.builder non accessible)
            return (AlertDialog) declaredConstructor.newInstance(context);
        } catch (Exception unused) {
            return null;
        }
    }
