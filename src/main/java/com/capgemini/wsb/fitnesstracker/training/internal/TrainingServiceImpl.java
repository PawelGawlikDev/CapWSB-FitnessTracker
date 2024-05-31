package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// TODO: Provide Impl
@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingProvider {

    private final TrainingRepository trainingRepository;

    private final UserRepository userRepository;

    @Override
    public Training getTraining(final Long trainingId) {
        return trainingRepository.getReferenceById(trainingId);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getAllTrainingsForUser(long userId) {
        return trainingRepository.findAll().stream().filter(training -> training.getUser().getId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public Training addTraining(Training training) {
        if (training.getId() != null) {
            throw new IllegalArgumentException("Training has already DB ID, update is not permitted!");
        }

        User user = training.getUser();
        if (user.getId() == null) {
            userRepository.save(user);
        }

        return trainingRepository.save(training);
    }


    @Override
    public List<Training> getAllFinishTrainings(Date date){
        return trainingRepository.findAll().stream()
                .filter(training -> training.getEndTime().compareTo(date) > 0)
                .toList();
    }

    @Override
    public List<Training> getAllTrainingTypes(ActivityType activity) {
        return trainingRepository.findAll().stream().filter(training -> training.getActivityType().equals(activity)).collect(Collectors.toList());
    }

    public void updateActivity(long id, ActivityType newActivityType) {
        Training training = trainingRepository.getReferenceById(id);
        training.setActivityType(newActivityType);
    }
    public void deleteUserTrainings(long userId) {
        List<Training> trainings = trainingRepository.findAll().stream()
                .filter(training -> training.getUser().getId().equals(userId))
                .toList();
        trainingRepository.deleteAll(trainings);
    }

    public Training updateTraining(Training training) {
        return trainingRepository.save(training);
    }
}
