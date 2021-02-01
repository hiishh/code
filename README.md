# H Code Snippets

Small snippets of code, that can be useful for modding


### Usage
## Access private AlertDialog 
A step by step series of examples that tell you how to get a development env running


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
