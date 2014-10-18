//package ebook.parser;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import ebook.EBook;
//
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//
//	public class AsyncParser extends AsyncTask<String, Boolean, EBook> {
//		private ProgressDialog progressDialog;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			progressDialog = new ProgressDialog(getActivity());
//			progressDialog.setMessage("Opening book. Please wait...");
//			progressDialog.setIndeterminate(true);
//			progressDialog.show();
//		}
//
//		@Override
//		protected EBook doInBackground(String... params) {
//			Parser parser = new InstantParser();
//			parser.parse(params[0]);
//			return parser.getEBoook();
//		}
//
//		@Override
//		protected void onPostExecute(EBook result) {
//			super.onPostExecute(result);
//			progressDialog.hide();
//		}
//	}
