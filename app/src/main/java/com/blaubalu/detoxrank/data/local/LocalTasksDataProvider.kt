package com.blaubalu.detoxrank.data.local

import com.blaubalu.detoxrank.data.task.Task
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.data.task.TaskIconCategory
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_10_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_250_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_READ_50_PAGES
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_10_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_3_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_5_KM
import com.blaubalu.detoxrank.ui.utils.Constants.ID_RUN_7_KM


/**
 * Data provider for Task. Used to fill the database with tasks
 */
object LocalTasksDataProvider {

    /**
     * Grammar fixes for descriptions that already shipped: old -> new.
     * Applied by the catalog sync before inserting missing tasks, so existing
     * databases get the corrected texts without duplicating tasks.
     */
    val renamedTasks = mapOf(
        "Do 40 Jumping Jacks" to "Do 40 jumping jacks",
        "Prepare healthy dinner" to "Prepare a healthy dinner",
        "Make salad" to "Make a salad",
        "Make a lemonade" to "Make lemonade",
        "Eat at least 3 pieces of vegetable" to "Eat at least 3 servings of vegetables",
        "Collect a small sample of 5 different trees' branches" to
                "Collect small branch samples from 5 different trees",
        "Observe dawn 2 times" to "Observe dawn twice",
        "Do stretching exercises 2 times" to "Do stretching exercises twice",
        "Jump rope 2 times" to "Jump rope twice",
        "Play a sport with a friend 2 times" to "Play a sport with a friend twice",
        "Meditate each week at least once" to "Meditate at least once each week",
        "Exercise each week at least once" to "Exercise at least once each week",
        "Exercise at least 3 times for 10 minutes" to "Exercise for 10 minutes at least 3 times",
        "Visit some place you always wanted to go" to "Visit a place you've always wanted to go",
        "Take a photo of a blue, red, orange, green and a yellow building" to
                "Take a photo of a blue, a red, an orange, a green and a yellow building",
        "Make your phone grayscale for a week (there are apps that can do so)" to
                "Make your phone grayscale for a week (there are apps for that)"
    )

    val tasks = listOf(
        Task(
            description = "Eat an apple",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Exercise for at least 10 minutes",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do a household chore",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Cleaning
        ),
        Task(
            description = "Clean your room",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Cleaning
        ),
        Task(
            description = "Observe dusk",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Read at least 10 pages",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Reading,
            specialTaskID = ID_READ_10_PAGES
        ),
        Task(
            description = "Meditate",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Go for a run",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Running
        ),
        Task(
            description = "Don't eat sugar",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.UnhealthyFood
        ),
        Task(
            description = "Go for a short walk",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Walking
        ),
        Task(
            description = "Don't eat unhealthy food",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.UnhealthyFood
        ),
        Task(
            description = "Prepare a healthy dinner",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Clean your table",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Cleaning
        ),
        Task(
            description = "Make a journal entry",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Pick a flower",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Fun
        ),
        Task(
            description = "Take a cold shower",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Eat a banana",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Eat an orange",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Eat your favorite fruit",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Drink a glass of water",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Eat a vegetable of your choice",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Do 15 push-ups",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do 30 sit-ups",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do 1 minute of planking",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do 40 jumping jacks",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Don't use social media (only if absolutely necessary)",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Don't use video streaming platforms",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Make a salad",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Touch grass",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Touch a tree",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Jump rope for 2 minutes",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Observe sunlight and nature for at least 10 minutes",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "No phone for the first 30 minutes after waking up",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "No phone during meals",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Leave your phone in another room while working or studying",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Sit in silence for 5 minutes — no phone, no music",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Take a walk without music or podcasts — just observe",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Walking
        ),
        Task(
            description = "Make your bed",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Cleaning
        ),
        Task(
            description = "Stretch for 5 minutes after waking up",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do 20 squats",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do 20 lunges",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Take the stairs instead of the elevator today",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Go to bed before 11 PM",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "No caffeine after noon",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "No snacking between meals",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.UnhealthyFood
        ),
        Task(
            description = "Take a 10-minute walk after a meal",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Walking
        ),
        Task(
            description = "Write down 3 things you're grateful for",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Call (don't text) a friend or family member",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Fun
        ),
        Task(
            description = "Give someone a sincere compliment",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Fun
        ),
        Task(
            description = "Learn 5 new words in a foreign language",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Reading
        ),
        Task(
            description = "Write tomorrow's to-do list before bed",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Spend 15 minutes on a hobby",
            durationCategory = TaskDurationCategory.Daily,
            iconCategory = TaskIconCategory.Fun
        ),


        Task(
            description = "Run 3km (in one go)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Running,
            specialTaskID = ID_RUN_3_KM
        ),
        Task(
            description = "Run 5km (in one go)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Running,
            specialTaskID = ID_RUN_5_KM
        ),
        Task(
            description = "Eat at least 3 pieces of fruit",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Learn how to prepare a new food",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Go for a hike",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Prepare lunch at least 2 times",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Exercise for 10 minutes at least 3 times",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Meditate 3 times",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Go for a hike in the forest",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Read 50 pages",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Reading,
            specialTaskID = ID_READ_50_PAGES
        ),
        Task(
            description = "Go for a bike ride (or a long walk if you don't have one)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Cycling
        ),
        Task(
            description = "Do a sports activity at least 2 times",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Cycling
        ),
        Task(
            description = "Take a walk in the park",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Walking
        ),
        Task(
            description = "Meditate in the park",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Make at least 3 diary/journal entries",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Draw something creative",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Practice a foreign language",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Reading
        ),
        Task(
            description = "Prepare a smoothie",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Do yoga 2 times",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Play a sport with a friend",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Sports
        ),
        Task(
            description = "Make lemonade",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Photograph street art",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Photography
        ),
        Task(
            description = "Drink only water",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Observe dawn twice",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Make your PC web browser grayscale for a week (e.g. through an extension)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Make your phone grayscale for a week (there are apps for that)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Do 100 push-ups cumulatively",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Do 200 sit-ups cumulatively",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Exercise outside (or go for a run if you can't)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Photograph an animal (not your own)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Photography
        ),
        Task(
            description = "Eat at least 3 servings of vegetables",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Don't eat sweets",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.UnhealthyFood
        ),
        Task(
            description = "Collect leaves from 7 different trees",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Take a photo of 7 different flowers",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Photography
        ),
        Task(
            description = "Take a photo of a blue, a red, an orange, a green and a yellow building",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Photography
        ),
        Task(
            description = "Collect small branch samples from 5 different trees",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Do stretching exercises twice",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Jump rope twice",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Have 3 screen-free evenings (no screens after 9 PM)",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Do a 24-hour social media detox on a weekend day",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Take a phone-free walk 3 times",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Walking
        ),
        Task(
            description = "Spend one afternoon completely offline",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Wake up at the same time every day this week",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Go to sleep before 11 PM at least 4 nights",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Declutter one drawer or shelf",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Cleaning
        ),
        Task(
            description = "Fix something you've been putting off",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Other
        ),
        Task(
            description = "Do 150 squats cumulatively",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Try a sport or workout you've never done",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Sports
        ),
        Task(
            description = "Bake something from scratch",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Write a letter or long message to someone you appreciate",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Visit a library or bookstore",
            durationCategory = TaskDurationCategory.Weekly,
            iconCategory = TaskIconCategory.Places
        ),


        Task(
            description = "Visit a new restaurant",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Places
        ),
        Task(
            description = "Visit a lake",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Go stargazing in the nature",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Play a sport with a friend twice",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Sports
        ),
        Task(
            description = "Play board games with friends",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Fun
        ),
        Task(
            description = "Organize a fun whole-day activity with friends",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Fun
        ),
        Task(
            description = "Go for a long hike (at least 10km)",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Eat at least 3 pieces of fruit each week",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Read a book (at least 250 pages)",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Reading,
            specialTaskID = ID_READ_250_PAGES
        ),
        Task(
            description = "Go for a swim",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Swimming
        ),
        Task(
            description = "Meditate at least once each week",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Visit a museum",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Places
        ),
        Task(
            description = "Visit a place you've always wanted to go",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Places
        ),
        Task(
            description = "Go on a trip",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Exercise at least once each week",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Run 7km (in one go)",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Running,
            specialTaskID = ID_RUN_7_KM
        ),
        Task(
            description = "Paint a painting",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Creating,
        ),
        Task(
            description = "Make a photo collage of your own",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Photography
        ),
        Task(
            description = "Do one full digital-detox day (no screens from dawn to dusk)",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Exercise at least 3 times each week",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Exercise
        ),
        Task(
            description = "Visit a town or neighborhood you've never been to",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Places
        ),
        Task(
            description = "Watch a sunrise from a nice viewpoint",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Hiking
        ),
        Task(
            description = "Declutter and donate a bag of things you don't use",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Cleaning
        ),
        Task(
            description = "Cook a three-course meal for someone",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.HealthyFood
        ),
        Task(
            description = "Read a book from a genre you never touch",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Reading
        ),
        Task(
            description = "Write one page about your month",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Plant something and keep it alive",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Attend a live event (concert, theatre, match)",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Music
        ),
        Task(
            description = "Meet a friend you haven't seen in over a month",
            durationCategory = TaskDurationCategory.Monthly,
            iconCategory = TaskIconCategory.Fun
        ),

        Task(
            description = "Run 10km (in one go)",
            durationCategory = TaskDurationCategory.Special,
            iconCategory = TaskIconCategory.Running,
            specialTaskID = ID_RUN_10_KM
        ),
        Task(
            description = "Create your own painting exposition (at least 5 paintings)",
            durationCategory = TaskDurationCategory.Special,
            iconCategory = TaskIconCategory.Creating
        ),
        Task(
            description = "Learn a new musical instrument",
            durationCategory = TaskDurationCategory.Special,
            iconCategory = TaskIconCategory.Music
        ),
        Task(
            description = "Go a full weekend without your phone",
            durationCategory = TaskDurationCategory.Special,
            iconCategory = TaskIconCategory.Health
        ),
        Task(
            description = "Meditate 30 days in a row",
            durationCategory = TaskDurationCategory.Special,
            iconCategory = TaskIconCategory.Meditation
        ),
        Task(
            description = "Finish a 500+ page book",
            durationCategory = TaskDurationCategory.Special,
            iconCategory = TaskIconCategory.Reading
        )
    )
}
