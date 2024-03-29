package com.bionx.res.background;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bionx.res.R;
import com.bionx.res.activity.FileManager;

public class EventHandler implements OnClickListener {
	/*
	 * Unique types to control which file operation gets performed in the
	 * background
	 */
	private static final int SEARCH_TYPE = 0x00;
	private static final int COPY_TYPE = 0x01;
	private static final int UNZIP_TYPE = 0x02;
	private static final int UNZIPTO_TYPE = 0x03;
	private static final int ZIP_TYPE = 0x04;
	private static final int DELETE_TYPE = 0x05;
	private final Context mContext;
	private final FileManager mFileMang;
	private ThumbnailCreator mThumbnail;
	private TableRow mDelegate;

	private boolean multi_select_flag = false;
	private boolean delete_after_copy = false;
	private boolean thumbnail_flag = true;
	private int mColor = Color.WHITE;

	// the list used to feed info into the array adapter and when multi-select
	// is on
	private ArrayList<String> mDataSource, mMultiSelectData;
	private TextView mPathLabel;
	private TextView mInfoLabel;

	/**
	 * Creates an EventHandler object. This object is used to communicate most
	 * work from the Main activity to the FileManager class.
	 * 
	 * @param context
	 *            The context of the main activity e.g Main
	 * @param manager
	 *            The FileManager object that was instantiated from Main
	 */
	public EventHandler(Context context, final FileManager manager) {
		mContext = context;
		mFileMang = manager;

		mDataSource = new ArrayList<String>(mFileMang.setHomeDir(Environment
				.getExternalStorageDirectory().getPath()));
	}

	/**
	 * This constructor is called if the user has changed the screen orientation
	 * and does not want the directory to be reset to home.
	 * 
	 * @param context
	 *            The context of the main activity e.g Main
	 * @param manager
	 *            The FileManager object that was instantiated from Main
	 * @param location
	 *            The first directory to display to the user
	 */
	public EventHandler(Context context, final FileManager manager,
			String location) {
		mContext = context;
		mFileMang = manager;

		mDataSource = new ArrayList<String>(
				mFileMang.getNextDir(location, true));
	}

	/**
	 * This method is called from the Main activity and this has the same
	 * reference to the same object so when changes are made here or there they
	 * will display in the same way.
	 * 
	 * @param adapter
	 *            The TableRow object
	 */
	public void setListAdapter(TableRow adapter) {
		mDelegate = adapter;
	}

	/**
	 * This method is called from the Main activity and is passed the TextView
	 * that should be updated as the directory changes so the user knows which
	 * folder they are in.
	 * 
	 * @param path
	 *            The label to update as the directory changes
	 * @param label
	 *            the label to update information
	 */
	public void setUpdateLabels(TextView path, TextView label) {
		mPathLabel = path;
		mInfoLabel = label;
	}

	/**
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		mColor = color;
	}

	/**
	 * Set this true and thumbnails will be used as the icon for image files.
	 * False will show a default image.
	 * 
	 * @param show
	 */
	public void setShowThumbnails(boolean show) {
		thumbnail_flag = show;
	}

	/**
	 * If you want to move a file (cut/paste) and not just copy/paste use this
	 * method to tell the file manager to delete the old reference of the file.
	 * 
	 * @param delete
	 *            true if you want to move a file, false to copy the file
	 */
	public void setDeleteAfterCopy(boolean delete) {
		delete_after_copy = delete;
	}

	/**
	 * Indicates whether the user wants to select multiple files or folders at a
	 * time. <br>
	 * <br>
	 * false by default
	 * 
	 * @return true if the user has turned on multi selection
	 */
	public boolean isMultiSelected() {
		return multi_select_flag;
	}

	/**
	 * Use this method to determine if the user has selected multiple
	 * files/folders
	 * 
	 * @return returns true if the user is holding multiple objects
	 *         (multi-select)
	 */
	public boolean hasMultiSelectData() {
		return (mMultiSelectData != null && mMultiSelectData.size() > 0);
	}

	/**
	 * Will search for a file then display all files with the search parameter
	 * in its name
	 * 
	 * @param name
	 *            the name to search for
	 */
	public void searchForFile(String name) {
		new BackgroundWork(SEARCH_TYPE).execute(name);
	}

	/**
	 * Will delete the file name that is passed on a background thread.
	 * 
	 * @param name
	 */
	public void deleteFile(String name) {
		new BackgroundWork(DELETE_TYPE).execute(name);
	}

	/**
	 * Will copy a file or folder to another location.
	 * 
	 * @param oldLocation
	 *            from location
	 * @param newLocation
	 *            to location
	 */
	public void copyFile(String oldLocation, String newLocation) {
		String[] data = { oldLocation, newLocation };

		new BackgroundWork(COPY_TYPE).execute(data);
	}

	/**
	 * 
	 * @param newLocation
	 */
	public void copyFileMultiSelect(String newLocation) {
		String[] data;
		int index = 1;

		if (mMultiSelectData.size() > 0) {
			data = new String[mMultiSelectData.size() + 1];
			data[0] = newLocation;

			for (String s : mMultiSelectData)
				data[index++] = s;

			new BackgroundWork(COPY_TYPE).execute(data);
		}
	}

	/**
	 * This will extract a zip file to the same directory.
	 * 
	 * @param file
	 *            the zip file name
	 * @param path
	 *            the path were the zip file will be extracted (the current
	 *            directory)
	 */
	public void unZipFile(String file, String path) {
		new BackgroundWork(UNZIP_TYPE).execute(file, path);
	}

	/**
	 * This method will take a zip file and extract it to another location
	 * 
	 * @param name
	 *            the name of the of the new file (the dir name is used)
	 * @param newDir
	 *            the dir where to extract to
	 * @param oldDir
	 *            the dir where the zip file is
	 */
	public void unZipFileToDir(String name, String newDir, String oldDir) {
		new BackgroundWork(UNZIPTO_TYPE).execute(name, newDir, oldDir);
	}

	/**
	 * Creates a zip file
	 * 
	 * @param zipPath
	 *            the path to the directory you want to zip
	 */
	public void zipFile(String zipPath) {
		new BackgroundWork(ZIP_TYPE).execute(zipPath);
	}

	/**
	 * this will stop our background thread that creates thumbnail icons if the
	 * thread is running. this should be stopped when ever we leave the folder
	 * the image files are in.
	 */
	public void stopThumbnailThread() {
		if (mThumbnail != null) {
			mThumbnail.setCancelThumbnails(true);
			mThumbnail = null;
		}
	}

	/**
	 * This method, handles the button presses of the top buttons found in the
	 * Main activity.
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.back_button:
			if (mFileMang.getCurrentDir() != "/") {
				if (multi_select_flag) {
					mDelegate.killMultiSelect(true);
					Toast.makeText(mContext, "Multi-select is now off",
							Toast.LENGTH_SHORT).show();
				}

				stopThumbnailThread();
				updateDirectory(mFileMang.getPreviousDir());
				if (mPathLabel != null)
					mPathLabel.setText(mFileMang.getCurrentDir());
			}
			break;

		case R.id.home_button:
			if (multi_select_flag) {
				mDelegate.killMultiSelect(true);
				Toast.makeText(mContext, "Multi-select is now off",
						Toast.LENGTH_SHORT).show();
			}

			stopThumbnailThread();
			updateDirectory(mFileMang.setHomeDir("/sdcard"));
			if (mPathLabel != null)
				mPathLabel.setText(mFileMang.getCurrentDir());
			break;

		case R.id.multiselect_button:
			if (multi_select_flag) {
				mDelegate.killMultiSelect(true);

			} else {
				LinearLayout hidden_lay = (LinearLayout) ((Activity) mContext)
						.findViewById(R.id.hidden_buttons);

				multi_select_flag = true;
				hidden_lay.setVisibility(View.VISIBLE);
			}
			break;

		/*
		 * three hidden buttons for multiselect
		 */
		case R.id.hidden_attach:
			/* check if user selected objects before going further */
			if (mMultiSelectData == null || mMultiSelectData.isEmpty()) {
				mDelegate.killMultiSelect(true);
				break;
			}

			ArrayList<Uri> uris = new ArrayList<Uri>();
			int length = mMultiSelectData.size();
			Intent mail_int = new Intent();

			mail_int.setAction(android.content.Intent.ACTION_SEND_MULTIPLE);
			mail_int.setType("application/mail");
			mail_int.putExtra(Intent.EXTRA_BCC, "");
			mail_int.putExtra(Intent.EXTRA_SUBJECT, " ");

			for (int i = 0; i < length; i++) {
				File file = new File(mMultiSelectData.get(i));
				uris.add(Uri.fromFile(file));
			}

			mail_int.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			mContext.startActivity(Intent.createChooser(mail_int,
					"Email using..."));

			mDelegate.killMultiSelect(true);
			break;

		case R.id.hidden_move:
		case R.id.hidden_copy:
			/* check if user selected objects before going further */
			if (mMultiSelectData == null || mMultiSelectData.isEmpty()) {
				mDelegate.killMultiSelect(true);
				break;
			}

			if (v.getId() == R.id.hidden_move)
				delete_after_copy = true;

			mInfoLabel.setText("Holding " + mMultiSelectData.size()
					+ " file(s)");

			mDelegate.killMultiSelect(false);
			break;

		case R.id.hidden_delete:
			/* check if user selected objects before going further */
			if (mMultiSelectData == null || mMultiSelectData.isEmpty()) {
				mDelegate.killMultiSelect(true);
				break;
			}

			final String[] data = new String[mMultiSelectData.size()];
			int at = 0;

			for (String string : mMultiSelectData)
				data[at++] = string;

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage("Are you sure you want to delete " + data.length
					+ " files? This cannot be " + "undone.");
			builder.setCancelable(false);
			builder.setPositiveButton("Delete",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new BackgroundWork(DELETE_TYPE).execute(data);
							mDelegate.killMultiSelect(true);
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mDelegate.killMultiSelect(true);
							dialog.cancel();
						}
					});

			builder.create().show();
			break;
		}
	}

	/**
	 * will return the data in the ArrayList that holds the dir contents.
	 * 
	 * @param position
	 *            the indext of the arraylist holding the dir content
	 * @return the data in the arraylist at position (position)
	 */
	public String getData(int position) {

		if (position > mDataSource.size() - 1 || position < 0)
			return null;

		return mDataSource.get(position);
	}

	/**
	 * called to update the file contents as the user navigates there phones
	 * file system.
	 * 
	 * @param content
	 *            an ArrayList of the file/folders in the current directory.
	 */
	public void updateDirectory(ArrayList<String> content) {
		if (!mDataSource.isEmpty())
			mDataSource.clear();

		for (String data : content)
			mDataSource.add(data);

		mDelegate.notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView topView;
		TextView bottomView;
		ImageView icon;
		ImageView mSelect; // multi-select check mark icon
	}

	public class TableRow extends ArrayAdapter<String> {
		private final int KB = 1024;
		private final int MG = KB * KB;
		private final int GB = MG * KB;
		private String display_size;
		private ArrayList<Integer> positions;
		private LinearLayout hidden_layout;

		public TableRow() {
			super(mContext, R.layout.tablerow, mDataSource);
		}

		public void addMultiPosition(int index, String path) {
			if (positions == null)
				positions = new ArrayList<Integer>();

			if (mMultiSelectData == null) {
				positions.add(index);
				add_multiSelect_file(path);

			} else if (mMultiSelectData.contains(path)) {
				if (positions.contains(index))
					positions.remove(new Integer(index));

				mMultiSelectData.remove(path);

			} else {
				positions.add(index);
				add_multiSelect_file(path);
			}

			notifyDataSetChanged();
		}

		/**
		 * This will turn off multi-select and hide the multi-select buttons at
		 * the bottom of the view.
		 * 
		 * @param clearData
		 *            if this is true any files/folders the user selected for
		 *            multi-select will be cleared. If false, the data will be
		 *            kept for later use. Note: multi-select copy and move will
		 *            usually be the only one to pass false, so we can later
		 *            paste it to another folder.
		 */
		public void killMultiSelect(boolean clearData) {
			hidden_layout = (LinearLayout) ((Activity) mContext)
					.findViewById(R.id.hidden_buttons);
			hidden_layout.setVisibility(View.GONE);
			multi_select_flag = false;

			if (positions != null && !positions.isEmpty())
				positions.clear();

			if (clearData)
				if (mMultiSelectData != null && !mMultiSelectData.isEmpty())
					mMultiSelectData.clear();

			notifyDataSetChanged();
		}

		public String getFilePermissions(File file) {
			String per = "-";

			if (file.isDirectory())
				per += "d";
			if (file.canRead())
				per += "r";
			if (file.canWrite())
				per += "w";

			return per;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder mViewHolder;
			int num_items = 0;
			String temp = mFileMang.getCurrentDir();
			File file = new File(temp + "/" + mDataSource.get(position));
			String[] list = file.list();

			if (list != null)
				num_items = list.length;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.tablerow, parent, false);

				mViewHolder = new ViewHolder();
				mViewHolder.topView = (TextView) convertView
						.findViewById(R.id.top_view);
				mViewHolder.bottomView = (TextView) convertView
						.findViewById(R.id.bottom_view);
				mViewHolder.icon = (ImageView) convertView
						.findViewById(R.id.row_image);
				mViewHolder.mSelect = (ImageView) convertView
						.findViewById(R.id.multiselect_icon);

				convertView.setTag(mViewHolder);

			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}

			if (positions != null && positions.contains(position))
				mViewHolder.mSelect.setVisibility(View.VISIBLE);
			else
				mViewHolder.mSelect.setVisibility(View.GONE);

			mViewHolder.topView.setTextColor(mColor);
			mViewHolder.bottomView.setTextColor(mColor);

			if (mThumbnail == null)
				mThumbnail = new ThumbnailCreator(52, 52);

			if (file != null && file.isFile()) {
				String ext = file.toString();
				String sub_ext = ext.substring(ext.lastIndexOf(".") + 1);

				/*
				 * This series of else if statements will determine which icon
				 * is displayed
				 */
				if (sub_ext.equalsIgnoreCase("pdf")) {
					mViewHolder.icon.setImageResource(R.drawable.pdf);

				} else if (sub_ext.equalsIgnoreCase("mp3")
						|| sub_ext.equalsIgnoreCase("wma")
						|| sub_ext.equalsIgnoreCase("m4a")
						|| sub_ext.equalsIgnoreCase("m4p")) {

					mViewHolder.icon.setImageResource(R.drawable.music);

				} else if (sub_ext.equalsIgnoreCase("png")
						|| sub_ext.equalsIgnoreCase("jpg")
						|| sub_ext.equalsIgnoreCase("jpeg")
						|| sub_ext.equalsIgnoreCase("gif")
						|| sub_ext.equalsIgnoreCase("tiff")) {

					if (thumbnail_flag && file.length() != 0) {
						Bitmap thumb = mThumbnail
								.isBitmapCached(file.getPath());

						if (thumb == null) {
							final Handler handle = new Handler(
									new Handler.Callback() {
										@Override
										public boolean handleMessage(Message msg) {
											notifyDataSetChanged();

											return true;
										}
									});

							mThumbnail.createNewThumbnail(mDataSource,
									mFileMang.getCurrentDir(), handle);

							if (!mThumbnail.isAlive())
								mThumbnail.start();

						} else {
							mViewHolder.icon.setImageBitmap(thumb);
						}

					} else {
						mViewHolder.icon.setImageResource(R.drawable.image);
					}

				} else if (sub_ext.equalsIgnoreCase("zip")
						|| sub_ext.equalsIgnoreCase("gzip")
						|| sub_ext.equalsIgnoreCase("gz")) {

					mViewHolder.icon.setImageResource(R.drawable.zip);

				} else if (sub_ext.equalsIgnoreCase("m4v")
						|| sub_ext.equalsIgnoreCase("wmv")
						|| sub_ext.equalsIgnoreCase("3gp")
						|| sub_ext.equalsIgnoreCase("mp4")) {

					mViewHolder.icon.setImageResource(R.drawable.movies);

				} else if (sub_ext.equalsIgnoreCase("doc")
						|| sub_ext.equalsIgnoreCase("docx")) {

					mViewHolder.icon.setImageResource(R.drawable.word);

				} else if (sub_ext.equalsIgnoreCase("xls")
						|| sub_ext.equalsIgnoreCase("xlsx")) {

					mViewHolder.icon.setImageResource(R.drawable.excel);

				} else if (sub_ext.equalsIgnoreCase("ppt")
						|| sub_ext.equalsIgnoreCase("pptx")) {

					mViewHolder.icon.setImageResource(R.drawable.ppt);

				} else if (sub_ext.equalsIgnoreCase("html")) {
					mViewHolder.icon.setImageResource(R.drawable.html32);

				} else if (sub_ext.equalsIgnoreCase("xml")) {
					mViewHolder.icon.setImageResource(R.drawable.xml32);

				} else if (sub_ext.equalsIgnoreCase("conf")) {
					mViewHolder.icon.setImageResource(R.drawable.config32);

				} else if (sub_ext.equalsIgnoreCase("apk")) {
					mViewHolder.icon.setImageResource(R.drawable.appicon);

				} else if (sub_ext.equalsIgnoreCase("jar")) {
					mViewHolder.icon.setImageResource(R.drawable.jar32);

				} else {
					mViewHolder.icon.setImageResource(R.drawable.text);
				}

			} else if (file != null && file.isDirectory()) {
				if (file.canRead() && file.list().length > 0)
					mViewHolder.icon.setImageResource(R.drawable.folder_full);
				else
					mViewHolder.icon.setImageResource(R.drawable.folder);
			}

			String permission = getFilePermissions(file);

			if (file.isFile()) {
				double size = file.length();
				if (size > GB)
					display_size = String
							.format("%.2f Gb ", size / GB);
				else if (size < GB && size > MG)
					display_size = String
							.format("%.2f Mb ", size / MG);
				else if (size < MG && size > KB)
					display_size = String
							.format("%.2f Kb ", size / KB);
				else
					display_size = String.format("%.2f bytes ", size);

				if (file.isHidden())
					mViewHolder.bottomView.setText("(hidden) | " + display_size
							+ " | " + permission);
				else
					mViewHolder.bottomView.setText(display_size + " | "
							+ permission);

			} else {
				if (file.isHidden())
					mViewHolder.bottomView.setText("(hidden) | " + num_items
							+ " items | " + permission);
				else
					mViewHolder.bottomView.setText(num_items + " items | "
							+ permission);
			}

			mViewHolder.topView.setText(file.getName());

			return convertView;
		}

		private void add_multiSelect_file(String src) {
			if (mMultiSelectData == null)
				mMultiSelectData = new ArrayList<String>();

			mMultiSelectData.add(src);
		}
	}

	/**
	 * A private inner class of EventHandler used to perform time extensive
	 * operations. So the user does not think the the application has hung,
	 * operations such as copy/past, search, unzip and zip will all be performed
	 * in the background. This class extends AsyncTask in order to give the user
	 * a progress dialog to show that the app is working properly.
	 * 
	 * (note): this class will eventually be changed from using AsyncTask to
	 * using Handlers and messages to perform background operations.
	 * 
	 * @author Joe Berria
	 */
	private class BackgroundWork extends
			AsyncTask<String, Void, ArrayList<String>> {
		private String file_name;
		private ProgressDialog pr_dialog;
		private int type;
		private int copy_rtn;

		private BackgroundWork(int type) {
			this.type = type;
		}

		/**
		 * This is done on the EDT thread. this is called before doInBackground
		 * is called
		 */
		@Override
		protected void onPreExecute() {

			switch (type) {
			case SEARCH_TYPE:
				pr_dialog = ProgressDialog.show(mContext, "Searching",
						"Searching current file system...", true, true);
				break;

			case COPY_TYPE:
				pr_dialog = ProgressDialog.show(mContext, "Copying",
						"Copying file...", true, false);
				break;

			case UNZIP_TYPE:
				pr_dialog = ProgressDialog.show(mContext, "Unzipping",
						"Unpacking zip file please wait...", true, false);
				break;

			case UNZIPTO_TYPE:
				pr_dialog = ProgressDialog.show(mContext, "Unzipping",
						"Unpacking zip file please wait...", true, false);
				break;

			case ZIP_TYPE:
				pr_dialog = ProgressDialog.show(mContext, "Zipping",
						"Zipping folder...", true, false);
				break;

			case DELETE_TYPE:
				pr_dialog = ProgressDialog.show(mContext, "Deleting",
						"Deleting files...", true, false);
				break;
			}
		}

		/**
		 * background thread here
		 */
		@Override
		protected ArrayList<String> doInBackground(String... params) {

			switch (type) {
			case SEARCH_TYPE:
				file_name = params[0];
				ArrayList<String> found = mFileMang.searchInDirectory(
						mFileMang.getCurrentDir(), file_name);
				return found;

			case COPY_TYPE:
				int len = params.length;

				if (mMultiSelectData != null && !mMultiSelectData.isEmpty()) {
					for (int i = 1; i < len; i++) {
						copy_rtn = mFileMang.copyToDirectory(params[i],
								params[0]);

						if (delete_after_copy)
							mFileMang.deleteTarget(params[i]);
					}
				} else {
					copy_rtn = mFileMang.copyToDirectory(params[0], params[1]);

					if (delete_after_copy)
						mFileMang.deleteTarget(params[0]);
				}

				delete_after_copy = false;
				return null;

			case UNZIP_TYPE:
				mFileMang.extractZipFiles(params[0], params[1]);
				return null;

			case UNZIPTO_TYPE:
				mFileMang.extractZipFilesFromDir(params[0], params[1],
						params[2]);
				return null;

			case ZIP_TYPE:
				mFileMang.createZipFile(params[0]);
				return null;

			case DELETE_TYPE:
				int size = params.length;

				for (int i = 0; i < size; i++)
					mFileMang.deleteTarget(params[i]);

				return null;
			}
			return null;
		}

		/**
		 * This is called when the background thread is finished. Like
		 * onPreExecute, anything here will be done on the EDT thread.
		 */
		@Override
		protected void onPostExecute(final ArrayList<String> file) {
			final CharSequence[] names;
			int len = file != null ? file.size() : 0;

			switch (type) {
			case SEARCH_TYPE:
				if (len == 0) {
					Toast.makeText(mContext, "Couldn't find " + file_name,
							Toast.LENGTH_SHORT).show();

				} else {
					names = new CharSequence[len];

					for (int i = 0; i < len; i++) {
						String entry = file.get(i);
						names[i] = entry.substring(entry.lastIndexOf("/") + 1,
								entry.length());
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setTitle("Found " + len + " file(s)");
					builder.setItems(names,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int position) {
									String path = file.get(position);
									updateDirectory(mFileMang.getNextDir(
											path.substring(0,
													path.lastIndexOf("/")),
											true));
								}
							});

					AlertDialog dialog = builder.create();
					dialog.show();
				}

				pr_dialog.dismiss();
				break;

			case COPY_TYPE:
				if (mMultiSelectData != null && !mMultiSelectData.isEmpty()) {
					multi_select_flag = false;
					mMultiSelectData.clear();
				}

				if (copy_rtn == 0)
					Toast.makeText(mContext,
							"File successfully copied and pasted",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(mContext, "Copy pasted failed",
							Toast.LENGTH_SHORT).show();

				pr_dialog.dismiss();
				mInfoLabel.setText("");
				break;

			case UNZIP_TYPE:
				updateDirectory(mFileMang.getNextDir(mFileMang.getCurrentDir(),
						true));
				pr_dialog.dismiss();
				break;

			case UNZIPTO_TYPE:
				updateDirectory(mFileMang.getNextDir(mFileMang.getCurrentDir(),
						true));
				pr_dialog.dismiss();
				break;

			case ZIP_TYPE:
				updateDirectory(mFileMang.getNextDir(mFileMang.getCurrentDir(),
						true));
				pr_dialog.dismiss();
				break;

			case DELETE_TYPE:
				if (mMultiSelectData != null && !mMultiSelectData.isEmpty()) {
					mMultiSelectData.clear();
					multi_select_flag = false;
				}

				updateDirectory(mFileMang.getNextDir(mFileMang.getCurrentDir(),
						true));
				pr_dialog.dismiss();
				mInfoLabel.setText("");
				break;
			}
		}
	}
}
