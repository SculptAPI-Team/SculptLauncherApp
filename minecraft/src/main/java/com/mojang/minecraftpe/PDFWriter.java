package com.mojang.minecraftpe;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes2-dex2jar.jar:com/mojang/minecraftpe/PDFWriter.class */
public class PDFWriter {
    private Rect mImageRect;
    private PdfDocument mOpenDocument;
    private Rect mPageRect = new Rect(0, 0, 612, 792);
    private TextPaint mPageTextPaint;
    private Rect mTextRect;
    private Rect mTitleRect;
    private TextPaint mTitleTextPaint;

    public PDFWriter() {
        Rect rect = new Rect(0, 0, this.mPageRect.width(), (int) (this.mPageRect.height() * 0.7f));
        this.mTitleRect = rect;
        rect.offset(0, (int) (this.mPageRect.height() * 0.3f));
        Rect rect2 = new Rect(this.mPageRect);
        this.mTextRect = rect2;
        rect2.inset(20, 20);
        Rect rect3 = new Rect(0, 0, 500, 500);
        this.mImageRect = rect3;
        rect3.offset(this.mPageRect.centerX() - this.mImageRect.centerX(), this.mPageRect.centerY() - this.mImageRect.centerY());
        Typeface typefaceCreateFromAsset = Typeface.DEFAULT_BOLD;
        try {
            typefaceCreateFromAsset = Typeface.createFromAsset(MainActivity.mInstance.getAssets(), "assets/fonts/Mojangles.ttf");
        } catch (Exception e) {
            System.out.println("Failed to load mojangles font: " + e.getMessage());
        }
        TextPaint textPaint = new TextPaint();
        this.mTitleTextPaint = textPaint;
        textPaint.setAntiAlias(true);
        this.mTitleTextPaint.setTextSize(64.0f);
        this.mTitleTextPaint.setColor(-16777216);
        this.mTitleTextPaint.setTypeface(typefaceCreateFromAsset);
        TextPaint textPaint2 = new TextPaint();
        this.mPageTextPaint = textPaint2;
        textPaint2.setAntiAlias(true);
        this.mPageTextPaint.setTextSize(32.0f);
        this.mPageTextPaint.setColor(-16777216);
    }

    private void _drawTextInRect(String str, PdfDocument.Page page, TextPaint textPaint, Rect rect, Layout.Alignment alignment) {
        StaticLayout staticLayout = new StaticLayout(str, textPaint, rect.width(), alignment, 1.0f, 0.0f, false);
        Canvas canvas = page.getCanvas();
        canvas.translate(rect.left, rect.top);
        staticLayout.draw(canvas);
    }

    private String _getExtension(String str) {
        int i;
        int iLastIndexOf = str.lastIndexOf(46);
        return (iLastIndexOf < 0 || (i = iLastIndexOf + 1) >= str.length()) ? "" : str.substring(i).toLowerCase();
    }

    private PdfDocument.PageInfo _getPageInfo(int i) {
        return new PdfDocument.PageInfo.Builder(this.mPageRect.width(), this.mPageRect.height(), i).create();
    }

    private String _readFileToString(String str) throws IOException {
        File file = new File(str);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[(int) file.length()];
        fileInputStream.read(bArr);
        fileInputStream.close();
        return new String(bArr);
    }

    public void closeDocument() {
        PdfDocument pdfDocument = this.mOpenDocument;
        if (pdfDocument != null) {
            pdfDocument.close();
            this.mOpenDocument = null;
        }
    }

    public boolean createDocument(String[] strArr, String str) {
        PdfDocument pdfDocument = this.mOpenDocument;
        if (pdfDocument != null) {
            pdfDocument.close();
        }
        PdfDocument pdfDocument2 = new PdfDocument();
        this.mOpenDocument = pdfDocument2;
        PdfDocument.Page pageStartPage = pdfDocument2.startPage(_getPageInfo(1));
        _drawTextInRect(str, pageStartPage, this.mTitleTextPaint, this.mTitleRect, Layout.Alignment.ALIGN_CENTER);
        this.mOpenDocument.finishPage(pageStartPage);
        for (int i = 0; i < strArr.length; i++) {
            String str2 = strArr[i];
            PdfDocument.Page pageStartPage2 = this.mOpenDocument.startPage(_getPageInfo(i + 2));
            try {
                String str_getExtension = _getExtension(str2);
                if (str_getExtension.equals("txt")) {
                    _drawTextInRect(_readFileToString(str2), pageStartPage2, this.mPageTextPaint, this.mTextRect, Layout.Alignment.ALIGN_NORMAL);
                } else {
                    if (!str_getExtension.equals("jpeg")) {
                        throw new UnsupportedOperationException("Unsupported extension from file: " + str2);
                    }
                    pageStartPage2.getCanvas().drawBitmap(BitmapFactory.decodeFile(str2), (Rect) null, this.mImageRect, (Paint) null);
                }
                this.mOpenDocument.finishPage(pageStartPage2);
            } catch (Exception e) {
                System.out.println("Failed to write page: " + e.getMessage());
                closeDocument();
                return false;
            }
        }
        return true;
    }

    public String getPicturesDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public boolean writeDocumentToFile(String str) throws IOException {
        try {
            this.mOpenDocument.writeTo(new FileOutputStream(str));
            return true;
        } catch (Exception e) {
            System.out.println("Failed to write pdf file: " + e.getMessage());
            return false;
        }
    }
}
