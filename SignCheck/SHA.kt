package h.hi.s

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class sh {
    private var a = ""
    @SuppressLint("PackageManagerGetSignatures")
    fun LiteSignature(ctx: Context): String {
        val packageInfo: PackageInfo = ctx.packageManager.getPackageInfo(
            ctx.packageName, PackageManager.GET_SIGNATURES
        )
        //note sample just checks the first signature
        for (signature in packageInfo.signatures) {
            a = sc.getSHA1(signature.toByteArray())
        }
        return a
    }
}
