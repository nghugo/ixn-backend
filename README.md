# Setup

1. Install **Java 17**.

2. Configure the necessary resources and environment variables:

    - **JWT Secret Key**:
        - Set an arbitrary value for the JWT secret key:
            - `IXN_APP_JWT_SECRET_KEY`

    - **AWS S3 Bucket**:
        - Create a bucket, record its name and set it as this variable:
            - `IXN_AWS_BUCKET_NAME`
        - Configure IAM access keys to allow access to the S3 bucket:
            - `IXN_AWS_USER_ACCESS_KEY`
            - `IXN_AWS_USER_SECRET_KEY`

    - **PostgreSQL Database**:
        - Set up your PostgreSQL database and configure the following environment variables:
            - `IXN_POSTGRES_PASSWORD`
            - `IXN_POSTGRES_URL`
            - `IXN_POSTGRES_USERNAME`

3. Build the project using Maven:
   `./mvnw install`
4. Start the Spring Boot application:
    `./mvnw spring-boot:run`

# Data Config

Data (users, content, and secrets) can be reset or used as normal by modifying `src/main/resources/application.yml`. You must choose enable either one option to start the server.
- Option 1: To reset the database to pre-populated data on server start, enable `ddl-auto: create` and `initialize: true`. This will delete all existing data and populate the database using the script in `src/main/java/com/hng/ixn/DataInitializer.java`. 
- Option 2: To use existing content as normal on server start, enable `ddl-auto: update`and `initialize: false`. This option should be enabled to run the database in production, as it does not reset the data when the server is restarted.

After choosing either option, start the server.

Note: files are stored separately in s3.

# Deployment

The simplest way to run the server is to use **render.com**, which is relatively cheap and has a nice user interface for automatic deployments. Follow these steps:

## 1. Create a GitHub Account
- Create a GitHub account and fork the backend repository, or you can clone the repository and push the code to your own GitHub repository. This allows you to develop locally with the cloned code.

## 2. Create a Render.com Web Service
- Go to **render.com** and create a web service (make sure to select the Docker runtime).
- Link the web service to your backend repository on GitHub, which will trigger automatic deployment. However, the server will not work properly yet.
- For more information, see [render.com web service setup](https://docs.render.com/web-services)

## 3. Create a PostgreSQL Database on Render.com
- Create a PostgreSQL database on **render.com**. You will receive a database username, password, and URL.
- Note down the credentials, as you will need them for the environment variable setup in step 5.
- The corresponding environment variables are:
  - `IXN_POSTGRES_PASSWORD`
  - `IXN_POSTGRES_URL`
  - `IXN_POSTGRES_USERNAME`
- For more information, see [render database docs](https://docs.render.com/databases)

## 4. Create an S3 Bucket in Amazon Web Services (AWS)
- Create an **S3 bucket** in **AWS** and name it `ixn-radio`. Also, create an IAM role that has access to this S3 bucket.
- You can follow the exact configuration in this [video](https://www.youtube.com/watch?v=FLIp6BLtwjk).
- Take note of your **Access Key ID** and **Secret Access Key** (at **3:11** in the video). These correspond to the following environment variables:
  - `IXN_AWS_USER_ACCESS_KEY`
  - `IXN_AWS_USER_SECRET_KEY`
  
- Edit the IAM policy to include the following JSON in the "Resource" section for the `ixn-radio` bucket (at **4:00** in the video):
  ```json
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": "s3:*",
        "Resource": [
          "arn:aws:s3:::ixn-radio",
          "arn:aws:s3:::ixn-radio/*"
        ]
      }
    ]
  }

You do not need to complete the test at the end of the video (**4:58**).

## 5. Set the Environment Variables on Render.com
- Set up the environment variables on your Render.com web service (from step 2). This can be done via the **Render.com** website. Recall from above, the environment variables are:
  - `IXN_APP_JWT_SECRET_KEY`: JWT Secret Key (set an arbitrary value).
  - `IXN_AWS_USER_ACCESS_KEY`: Access key for the IAM user with access to the `ixn-radio` S3 bucket.
  - `IXN_AWS_USER_SECRET_KEY`: Secret key for the IAM user with access to the `ixn-radio` S3 bucket.
  - `IXN_POSTGRES_URL`: The URL of the PostgreSQL server.
  - `IXN_POSTGRES_USERNAME`: The username of the PostgreSQL admin user.
  - `IXN_POSTGRES_PASSWORD`: The password for the PostgreSQL admin user.

- These environment variables serve the following purposes:
  - Set up the encryption authentication token for security needs.
  - Link the server to the PostgreSQL database (database for text).
  - Link the server to the Amazon S3 bucket (database for files).

## Additional Documentation
For more details on configuring environment variables for your web service on **render.com**, refer to the official documentation:  
[Render.com Environment Variables Setup](https://docs.render.com/configure-environment-variables)

