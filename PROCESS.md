![logo](doc/textLogo.png)
# Process Book

# Day 1 - 6 june
- Drew initial sketches of the design
- Created README.md

# Day 2 - 7 june
- Created the android studio project
- Made sketches for app flow and needed functions
![flow](doc/App flow.jpg)
![functions](doc/First idea diagram.jpg)

# Day 3 - 8 june
- Completed the prototype
- Completed DESIGN.md and the diagram

# Day 4 - 9 june
- Presented my ideas
- Added ideas from presentations to README.md
<br>

## Week 2
# Day 5 - 12 june
- Made planning for rest of the week
- Edited the design in android studio and DESING.md 
- Google books API can't request random books, so I will add a file with approx. 100 best sold book titles from [goodreads](http://www.goodreads.com/list/show/33934.Best_Selling_Books_of_All_Time).

# Day 6 - 13 june
- In some descriptions, the name of the book is written. It would be cool to deletes this from the description.
- Made the httprequesthelper and the asynctask. They recieve JSON data, but it's not handeled right.

# Day 7 - 14 june
- Answer class isn't necessary. 
- Plot and answers get set in their views.
- Answers get different color after answer is chosen.
- basic app functionality works, but the descriptions need a closer look.

# Day 8 - 15 june
### General ideas
TODO:

- Make responses from API better
	- Make sure he always gets the right book
	- Make sure he always finds something
- Erase some information from the plot, like the name of the book if it's in there or the name of the writer.
- Implement high score menu.
- App shouldn't reload when device is turned.
- When a new question is made, scrollview stays in old position. Should go back up.

POSSIBILITIES:

- Add a bookshelve with all the books that you've guessed correct. When clicked on the book, information appears and an link to google books. 
- Add a reinforcement like a nice animation or a free e-book when certain streaks are reached.
- Add different difficulties and genres.

### Today
- Implemented streaks.