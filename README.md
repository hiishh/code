# H Code Snippets

Small snippets of code, that can be useful for modding


## Usage
### Access private AlertDialog
This can be used if you want to access any private class, not just alertdialog, use this as an example.
```
AlertDialog privateDialog = = createAlertDialog(this.mContext //Context);
privateDialog.setTitle("TITLE");
privateDialog.setMessage("MSG");
privateDialog.setButton("OK", new DialogInterface.OnClickListener() {
public final void onClick(DialogInterface dialogInterface, int i) {
  Toast.makeText(mContext, "Done", Toast.LENGTH_LONG).show();
  }
});
privateDialog.show();
```
### SignCheck
Written in Kotlin + Java
Not 100% foolproof.

```
No usage ex yet.
```