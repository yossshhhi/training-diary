# Training Diary

A workout diary app that allows users to record their workouts, review them, and analyze their workout progress

1. **[Homework №1](https://github.com/yossshhhi/training-diary/pull/1)**
2. **[Homework №2](https://github.com/yossshhhi/training-diary/pull/2)**
3. **[Homework №3](https://github.com/yossshhhi/training-diary/pull/3)**
4. **[Homework №4](https://github.com/yossshhhi/training-diary/pull/4)**
5. **[Homework №5](https://github.com/yossshhhi/training-diary/pull/5)**

## Install

To install and run this project, follow these steps:

1. **Download the repository:**
    - Go to [the repository](https://github.com/yossshhhi/training-diary)
    - Click "Code" and select "Download ZIP"
    - Extract the ZIP file to your computer
2. **Start Docker Compose:**
   - Open a terminal or command prompt
   - Navigate to the extracted folder
   - Run:
   ```bash
    docker-compose up
   ```
3. **Run the `Application`:**
   - Open your IDE or text editor
   - Find the build.gradle file in the project structure
   - Ensure Gretty plugin is included in the plugins section of the `build.gradle` file
   - Run the `./gradlew appRun` command in the terminal or command prompt
   - Once the server is started, you can interact with the application by navigating to http://localhost:8080 in your web browser.


**To log in as administrator use**
- username: admin
- password: admin

# API Endpoints
## Administrative Operations

- `GET /admin/workouts`: Retrieves all existing workout records.
- `GET /admin/audits`: Retrieves all audit records.
- `POST /admin/add-extra-option-type`: Creates a new extra option type for workouts.
  ```json
  {
    "name": "New Option"
  }
  ```
- `POST /admin/add-workout-type`: Creates a new workout type.
  ```json
  {
    "name": "Interval Training"
  }
  ```

## Security Operations

- `POST /register`: Registers a new user.
  ```json
  {
    "username": "example",
    "password": "password"
  }
  ```

- `POST /login`: Authenticates a user and provides a token.
  ```json
  {
    "username": "example",
    "password": "password"
  }
  ```
## User Operations Endpoints

- `DELETE /user/workouts/delet`: Deletes a workout based on the provided ID.
  - Request Parameters: `id` (Long)

- `PATCH /user/workouts/edit`: Edits the details of an existing workout.
  ```json
  {
    "id": "workout ID"
    // updated workout details
  }
  ```
- `GET /user/statistic`: Retrieves workout statistics for a user over a specified number of days.
    - Request Parameters: `days` (Integer)

- `POST /user/record`: Records a workout for the specified user.
  ```json
  {
    "workoutTypeId": 3,
    "duration": 100,
    "burnedCalories": 200,
    "extraOptions": [
        {
            "typeId": 3,
            "value": 100
        }
    ]
  }
  ```

- `GET /user/extra-option-types`: Retrieves all extra option types available.

- `GET /user/workouts`: Retrieves all workouts recorded by a specific user.

- `GET /user/workout-types`: Retrieves all workout types defined in the system.

## Contact with me

- telegram: [@yossshhhi](https://t.me/yossshhhi)