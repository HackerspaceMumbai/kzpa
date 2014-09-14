package com.example.hack4change;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

public class FacebookFragment extends SherlockFragment {

	private ShareActionProvider mShareActionProvider;
	TextView tv;
	String thisone, line, line1, line2;
	boolean visible = true;
	boolean show = true;

	private static final String TAG = "FacebookFragment";
	private UiLifecycleHelper uiHelper;

	// code
	private Button shareButton, clearButton;
	ImageButton cameraButton;

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions", "publish_stream", "photo_upload",
					"manage_pages");
	private boolean pendingPublishReauthorization = false;
	Uri initialURI;
	ImageView mImageView;
	// directory name to store captured images and videos
	private Bitmap mBitmap;

	// gallery code
	protected static final int GALLERY_PICTURE = 1;
	protected static final int GALLERY_PICTURE_KITKAT = 1;
	private String selectedImagePath;
	private Intent pictureActionIntent = null;

	// code ends

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean firstboot = this.getActivity()
				.getSharedPreferences("BOOT_PREF", 0)
				.getBoolean("firstboot", true);
		if (firstboot) {

			this.getActivity().getSharedPreferences("BOOT_PREF", 0).edit()
					.putBoolean("firstboot", false).commit();
		}

		setRetainInstance(true);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fbfeed, container, false);
		setHasOptionsMenu(true);

		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setFragment(this);
		// authButton.setPublishPermissions(Arrays.asList("publish_actions",
		// "publish_stream"));
		authButton.setReadPermissions(Arrays.asList("read_stream",
				"user_groups", "photo_upload", "friends_groups"));
		// Session session = Session.getActiveSession();
		// if (session == null ) {
		// session = new
		// Session.Builder(getActivity()).setApplicationId(getString(R.string.APP_ID)).build();
		// Session.setActiveSession(session);
		// }
		return view;
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		tv = (TextView) getActivity().findViewById(R.id.fbwelcome);
		if (state.isOpened()) {

			Log.i(TAG, "Logged in");
			tv.setVisibility(View.VISIBLE);
			shareButton.setVisibility(View.VISIBLE);
			cameraButton.setVisibility(View.VISIBLE);

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out");
			tv.setVisibility(View.INVISIBLE);
			cameraButton.setVisibility(View.INVISIBLE);
			shareButton.setVisibility(View.INVISIBLE);

		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	// code
	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		// Log.d("A", "1");

		Session.openActiveSession(getActivity(), true,
				new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						if (session.isOpened()) {

							// make request to the /me API
							Request.newMeRequest(session,
									new Request.GraphUserCallback() {

										// callback after Graph API response
										// with user object
										@Override
										public void onCompleted(GraphUser user,
												Response response) {
											if (user != null
													&& getActivity() != null
													&& isVisible()) {
												ProfilePictureView profilePictureView = (ProfilePictureView) getActivity()
														.findViewById(
																R.id.profilePicture);
												profilePictureView
														.setProfileId(user
																.getId());
												TextView welcome = (TextView) getActivity()
														.findViewById(
																R.id.fbwelcome);
												welcome.setText("Hello, "
														+ user.getFirstName()
														+ "!");
											}
										}
									}).executeAsync();
						}
					}
				});
		mImageView = (ImageView) getActivity().findViewById(R.id.cameraPhoto);

		cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
						getActivity());
				myAlertDialog.setTitle("Upload Pictures Option");
				myAlertDialog
						.setMessage("How do you want to set your picture?");

				myAlertDialog.setPositiveButton("Gallery",
						new OnClickListener() {
							@TargetApi(19)
							public void onClick(DialogInterface arg0, int arg1) {
								Intent intent = new Intent();
								intent.setType("image/jpeg");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								startActivityForResult(Intent.createChooser(
										intent,
										getResources().getString(
												R.string.select_picture)),
										GALLERY_PICTURE);

							}
						});

				myAlertDialog.setNegativeButton("Camera",
						new OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								String _path = Environment
										.getExternalStorageDirectory()
										+ File.separator + "Hack4Good.jpg";
								File file = new File(_path);
								Uri outputFileUri = Uri.fromFile(file);
								Intent intent = new Intent(
										android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT,
										outputFileUri);
								startActivityForResult(intent, 1888);

							}
						});
				myAlertDialog.show();
			}
		});

		shareButton = (Button) view.findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
						getActivity());
				myAlertDialog.setTitle("Where to?");
				myAlertDialog.setMessage("Post update where?");

				myAlertDialog.setPositiveButton("My Timeline",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								toTimeline();
								Toast.makeText(getActivity(), "Processing..",
										Toast.LENGTH_LONG).show();
							}
						});

				myAlertDialog.setNegativeButton("Hack4Good Group",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								toHack4Good();
								Toast.makeText(getActivity(), "Processing..",
										Toast.LENGTH_LONG).show();

							}
						});
				myAlertDialog.show();

			}// onClick ends

			private void toTimeline() {
				// TODO Auto-generated method stub

				if (thisone == null) {

					publishStoryTimeline();
				} else if (thisone != null) {

					publishStoryTimeline(mImageView);
				}

			}

			private void toHack4Good() {
				// TODO Auto-generated method stub
				EditText hint = (EditText) getActivity().findViewById(
						R.id.editTextMessage);
				String share = hint.getText().toString();

				if (thisone == null) {

					publishStory();
				} else if (thisone != null) {

					publishStory(mImageView);
				}

			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(getActivity(), requestCode,
				resultCode, data);

		if (requestCode == 1888) {
			String _path = Environment.getExternalStorageDirectory()
					+ File.separator + "Hack4Good.jpg";
			thisone = _path;

			mBitmap = BitmapFactory.decodeFile(_path);
			if (mBitmap == null) {
				Toast.makeText(getActivity(), "Photo Corrupt! Try Again",
						Toast.LENGTH_LONG).show();
			} else { // Toast.makeText(getActivity(), "right text",
						// Toast.LENGTH_SHORT).show(); //
				// mBitmap.compress(Bitmap.CompressFormat.PNG, 100, mImageView);

				mImageView.setImageBitmap(mBitmap);

			}

		} else if (requestCode == GALLERY_PICTURE) {
			if (data != null) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String fileSrc = cursor.getString(columnIndex);
				cursor.close();
				/*
				 * Cursor cursor = null; cursor =
				 * getActivity().getContentResolver().query( data.getData(),
				 * null, null, null, null); if (cursor != null) {
				 * cursor.moveToFirst(); int idx =
				 * cursor.getColumnIndex(ImageColumns.DATA); String fileSrc =
				 * cursor.getString(idx);
				 */
				thisone = fileSrc;
				mBitmap = BitmapFactory.decodeFile(fileSrc); // load

				mImageView.setImageBitmap(mBitmap);

				// publishStory(mImageView);

			}
		}
	}

	private void publishStory(ImageView bmpImageCamera) {
		// TODO Auto-generated method stub
		Session session = Session.getActiveSession();
		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			String final_message = "Hey check out this awesome Photo!";

			EditText hint = (EditText) getActivity().findViewById(
					R.id.editTextMessage);
			String sharing = hint.getText().toString();
			if (sharing != null) {
				final_message = sharing;
			}

			BitmapDrawable drawable = (BitmapDrawable) bmpImageCamera
					.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream2);
			byte[] imageInByte = stream2.toByteArray();
			Bundle postParams = new Bundle();
			postParams.putByteArray("picture", imageInByte);
			postParams.putString("message", final_message);
			// postParams.putString("caption", "and caption too :D");

			Request.Callback callback = new Request.Callback() {

				public void onCompleted(Response response) {
					String postId = null;
					try {
						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();

						postId = graphResponse.getString("id");
					}// try ends
					catch (Exception e) {
						Toast.makeText(
								getActivity(),
								"Log in again or join the Hack4Good  open group",
								Toast.LENGTH_LONG).show();
						Log.i("JSON error", e.getMessage());
					}// catch ends

					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getActivity().getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					}// if ends
					else {
						Toast.makeText(getActivity().getApplicationContext(),
								"Image successfully posted", Toast.LENGTH_SHORT)
								.show();

					}// else ends
				}// on completed ends
			};
			// 1406291946260926
			// open group => 360793587411984
			// test post ==> 1443922975825622
			// pokemon = 450191461747663
			try {
				Request request = new Request(session,
						"360793587411984/photos", postParams, HttpMethod.POST,
						callback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			} catch (NullPointerException er) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Retry, somethings wrong!", Toast.LENGTH_LONG).show();
				// mImageView.setImageBitmap(null);
			}// catch ends
			thisone = null;
		}// new request call back ends
			// }// if img!=null ends
		else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Something wrong with the network, try again",
					Toast.LENGTH_LONG).show();
			//

		}
		EditText hint = (EditText) getActivity().findViewById(
				R.id.editTextMessage);
		mImageView.setImageBitmap(null);
		hint.setText(null);
	}// publish story ends

	private void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			String final_message = "Hey check out this awesome Photo!";

			EditText hint = (EditText) getActivity().findViewById(
					R.id.editTextMessage);
			String sharing = hint.getText().toString();
			if (sharing != null) {
				final_message = sharing;
			}
			if (hint.getText().toString() == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Enter a message first!", Toast.LENGTH_LONG).show();
			} else {

				Bundle postParams = new Bundle();
				postParams.putString("message", final_message);
				/*
				 * postParams.putString("name", "Facebook SDK for Android");
				 * postParams.putString("caption",
				 * "Build great social apps and get more installs.");
				 * postParams.putString("description",
				 * "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps."
				 * ); postParams.putString("link",
				 * "https://developers.facebook.com/android");
				 * postParams.putString("picture",
				 * "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png"
				 * );
				 */
				Request.Callback callback = new Request.Callback() {

					public void onCompleted(Response response) {
						String postId = null;
						try {
							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();

							postId = graphResponse.getString("id");
						} catch (NullPointerException er) {
							Toast.makeText(
									getActivity(),
									"Join the Hack4Good  group or selected data not found!",
									Toast.LENGTH_LONG).show();
						}

						catch (Exception e) {
							Toast.makeText(
									getActivity(),
									"Log in again or join the Hack4Good  open group",
									Toast.LENGTH_LONG).show();
							Log.i("JSON error", e.getMessage());
						}
						FacebookRequestError error = response.getError();
						if (error != null) {
							Toast.makeText(
									getActivity().getApplicationContext(),
									error.getErrorMessage(), Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Message successfully posted",
									Toast.LENGTH_LONG).show();
						}
					}
				};
				// donkeys- 418128934986608
				// pokemon - 450191461747663
				try {
					Request request = new Request(session,
							"360793587411984/feed", postParams,
							HttpMethod.POST, callback);
					RequestAsyncTask task = new RequestAsyncTask(request);
					task.execute();
				} catch (Exception er) {
					Toast.makeText(getActivity().getApplicationContext(),
							"Something wrong with the network!",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		EditText hint = (EditText) getActivity().findViewById(
				R.id.editTextMessage);
		mImageView.setImageBitmap(null);
		hint.setText(null);

	}

	private void publishStoryTimeline(ImageView bmpImageCamera) {
		// TODO Auto-generated method stub
		Session session = Session.getActiveSession();
		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			String final_message = "Hey check out this awesome Photo!";

			EditText hint = (EditText) getActivity().findViewById(
					R.id.editTextMessage);
			String sharing = hint.getText().toString();
			if (sharing != null) {
				final_message = sharing;
			}

			BitmapDrawable drawable = (BitmapDrawable) bmpImageCamera
					.getDrawable();
			Bitmap bitmap = drawable.getBitmap();
			ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream2);
			byte[] imageInByte = stream2.toByteArray();
			Bundle postParams = new Bundle();
			postParams.putByteArray("picture", imageInByte);
			postParams.putString("message", final_message);
			// postParams.putString("caption", "and caption too :D");

			Request.Callback callback = new Request.Callback() {

				public void onCompleted(Response response) {
					String postId = null;
					try {
						JSONObject graphResponse = response.getGraphObject()
								.getInnerJSONObject();

						postId = graphResponse.getString("id");
					}// try ends
					catch (Exception e) {
						Toast.makeText(
								getActivity(),
								"Log in again or join the Hack4Good  open group",
								Toast.LENGTH_LONG).show();
						Log.i("JSON error", e.getMessage());
					}// catch ends

					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getActivity().getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					}// if ends
					else {
						Toast.makeText(getActivity().getApplicationContext(),
								"Image successfully posted", Toast.LENGTH_SHORT)
								.show();

					}// else ends
				}// on completed ends
			};
			// 1406291946260926
			// open group => 360793587411984
			// test post ==> 1443922975825622
			// pokemon = 450191461747663
			try {
				Request request = new Request(session, "me/photos", postParams,
						HttpMethod.POST, callback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			} catch (NullPointerException er) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Retry, somethings wrong!", Toast.LENGTH_LONG).show();
				// mImageView.setImageBitmap(null);
			}// catch ends
			thisone = null;
		}// new request call back ends
			// }// if img!=null ends
		else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Something wrong with the network, try again",
					Toast.LENGTH_LONG).show();
			//

		}
		EditText hint = (EditText) getActivity().findViewById(
				R.id.editTextMessage);
		mImageView.setImageBitmap(null);
		hint.setText(null);
	}// publish story ends

	private void publishStoryTimeline() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			String final_message = "Hey check out this awesome Photo!";

			EditText hint = (EditText) getActivity().findViewById(
					R.id.editTextMessage);
			String sharing = hint.getText().toString();
			if (sharing != null) {
				final_message = sharing;
			}
			if (hint.getText().toString().equals("")) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Enter a message first!", Toast.LENGTH_LONG).show();
			} else {

				Bundle postParams = new Bundle();
				postParams.putString("message", final_message);
				/*
				 * postParams.putString("name", "Facebook SDK for Android");
				 * postParams.putString("caption",
				 * "Build great social apps and get more installs.");
				 * postParams.putString("description",
				 * "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps."
				 * ); postParams.putString("link",
				 * "https://developers.facebook.com/android");
				 * postParams.putString("picture",
				 * "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png"
				 * );
				 */
				Request.Callback callback = new Request.Callback() {

					public void onCompleted(Response response) {
						String postId = null;
						try {
							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();

							postId = graphResponse.getString("id");
						} catch (NullPointerException er) {
							Toast.makeText(
									getActivity(),
									"Join the Hack4Good  group or selected data not found!",
									Toast.LENGTH_LONG).show();

							Log.d("oncompleted", "null pointer");
						}

						catch (Exception e) {
							Toast.makeText(
									getActivity(),
									"Log in again or join the Hack4Good  open group",
									Toast.LENGTH_LONG).show();
							Log.i("JSON error", e.getMessage());
						}
						FacebookRequestError error = response.getError();
						if (error != null) {
							// Toast.makeText(
							// getActivity().getApplicationContext(),
							// error.getErrorMessage(), Toast.LENGTH_SHORT)
							// .show();
						} else {
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Message successfully posted",
									Toast.LENGTH_LONG).show();
						}
					}
				};
				// donkeys- 418128934986608
				// pokemon - 450191461747663
				try {
					Request request = new Request(session, "me/feed",
							postParams, HttpMethod.POST, callback);
					RequestAsyncTask task = new RequestAsyncTask(request);
					task.execute();
				} catch (Exception er) {
					Toast.makeText(getActivity().getApplicationContext(),
							"Something wrong with the network!",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		EditText hint = (EditText) getActivity().findViewById(
				R.id.editTextMessage);
		mImageView.setImageBitmap(null);
		hint.setText(null);

	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

}
