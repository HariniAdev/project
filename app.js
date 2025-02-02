const video = document.getElementById('video');
const captureButton = document.getElementById('capture');
const photoContainer = document.getElementById('photo-container');
const photo = document.getElementById('photo');
const canvas = document.getElementById('canvas');
const context = canvas.getContext('2d');

// Set up camera stream
navigator.mediaDevices.getUserMedia({ video: true })
  .then((stream) => {
    video.srcObject = stream;
  })
  .catch((error) => {
    console.error("Error accessing the camera: ", error);
  });

// Capture photo when the button is clicked
captureButton.addEventListener('click', () => {
  // Set canvas dimensions to match video feed
  canvas.width = video.videoWidth;
  canvas.height = video.videoHeight;

  // Draw the current video frame to the canvas
  context.drawImage(video, 0, 0, canvas.width, canvas.height);

  // Convert the canvas to an image (base64 format) and display it
  const imageUrl = canvas.toDataURL('image/png');
  photo.src = imageUrl;
  photoContainer.style.display = 'block'; // Show the photo
});
