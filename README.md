<h1>MarkMe - A Face recognization application to maintain attendence of employees</h1> 

<h3>How?</h3>
<p>Using Microsoft Azure Face API,https://docs.microsoft.com/en-in/azure/cognitive-services/face/overview.</p>

The <strong>Identify API</strong> can be used to identify a detected face against a database of people. This may be useful for automatic image tagging in photo management software. You create the database in advance, and it can be edited over time.

The following image depicts an example of a database named "myfriends." Each group may contain up to 1,000,000 different person objects, and each person object can have up to 248 faces registered.

<img src="https://docs.microsoft.com/en-in/azure/cognitive-services/face/images/person.group.clare.jpg">

<h3>Pre-requisites</h3>
<ul>
<li>1. You should have a valid Microsoft Azure account to use Face API. This project uses a trail <strong>API_ENDPOINT</strong> and <strong>SUBSCRIPTION_KEY</strong>.</li>
<li>2. Create a  "PersonGroup" to hold the source photos of the people.</li>
<li>3. Create a "PersonGroup.Person" for each employee and add it to the group.</li>
<li>4. Train the PersonGroup (it is asynchronous).</li>
<li>5. Get the TrainingStatus - should return "status": "succeeded".</li>
<li>6. Get the faceId of the query face using Face.Detect API. It will return a faceId to be used in step 6.</li>
<li>7. Run Face.Verify API with the personGroupId and the query "faceId".</li>
</ul>
