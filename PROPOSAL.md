![logo](doc/textLogo.png)

Let's guess the book title! <br>

Julia Anten<br>
Programmeerproject<br>
Minor Programmeren UvA

## Project Proposal

The app I'm proposing is a game for book lovers and other people who have the general feeling of being bored. It is something to waste some time with, like most games. You can immediately start the game or pick a genre first. The game shows you a plot summary and then asks you the guess the title that belongs with it. It will be multiple choice with three different title options per question. After an answer is chosen, the right answer will turn green and the wrong answers will turn red. If the user chose the right answer their streak will be increased by one. When an answer is wrong the streak will reset and start at zero again. The three highest streaks will be saved.

### Information Gathering
All the necessary information like the book plot and title will come from the Google Books [API](https://developers.google.com/books/). The information will come in the form of a Json from which I will separate the title and plot information for each book. 

### Parts and Features
The app will consist of a start screen on which the game will be explained. There will also be a button to a high score menu in the form of a pop-up. The game screen will have the same action bar as the start screen. Furthermore, there will be a book plot and three different answers from which one is right. There is also a counter that displays the current streak. Lastly, there will be a class that helps with getting information from the database and a class that actually gets information from the database.

### Possible Difficulties
There could be a few possible difficulties with this app. The first one could be how to check if an answer is correct. Another one is what happens when the API does not longer give any information. Lastly there needs to be something like a filter to make sure there will be no very obscure books that nobody know. 

### Comparison
I couldn't really find an app that does the same, but there are some sites. Like [this](https://www.sporcle.com/games/getschooled/guess-the-book-by-the-plot/) one. It gives more answer options than three and the plots are very limited and there is a limited amount of questions, but the idea is the same. 

### Minimum Viable Product
The start screen and the game screen would be part of the minimum viable product. The high scores could be left out. Other optionals idea's are making different difficulties or switching the idea around, giving a title and let users guess the plot. Another idea would be to incorporate different genres that users could play the game in. This could also be done with years. Maybe also against your friends. Or a link to the book to buy online or read more about the book if anybody wants.

### User interface*
![Sketch](doc/sketches.jpg)
<sub>*See design document for updated sketches.</sub>
<br><br><br><br>
