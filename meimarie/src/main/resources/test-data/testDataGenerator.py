import datetime
import random
import uuid


def random_date(start, end):
    """
    This function will return a random datetime between two datetime
    objects.
    """
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    return start + datetime.timedelta(seconds=random_second)


if __name__ == '__main__':

    tagsOut = ["#food", "#lidl", "#breakfast", "#misc", "#phone", "#amazon", "#tech stuff",
               "#living", "#rent", "#gas", "#petrol", "#transport", "#pub", "#dinner"]
    tagsIn = ["#present", "#withdrawal", "#poker win"]
    transactionTypes = ["CA", "CC", "WT"]

    startDate = datetime.datetime.strptime('1/1/2015', '%d/%m/%Y')
    endDate = datetime.datetime.strptime('31/12/2015', '%d/%m/%Y')

    firstLine = "{\"index\":{\"_index\":\"meimarie\", \"_type\":\"transaction\"}}\n"

    file = open('result.json', 'w')
    for i in range(1, 100):
        currUuid = uuid.uuid4()
        date = random_date(startDate, endDate).strftime("%Y-%m-%d")
        transactionType = transactionTypes[random.randint(0, 2)]
        amount = random.randint(-250, 100)
        tagsList = list()
        if amount < 0:
            numTags = random.randint(1, 5)
            for i in range(0, numTags):
                tagsList.append(tagsOut[random.randint(0, len(tagsOut)-1)])
        else:
            tagsList.append(tagsIn[random.randint(0, 2)])
        tags = '\", \"'.join(tagsList)

        secondLine = "{ \"id\":\"" + str(currUuid) + "\", \"tags\":[\"" + tags + "\"], \"date\": \"" + date \
                     + "\",  \"transactionType\": \"" + transactionType + "\", \"amount\": " + str(amount) + "}\n"
        file.write(firstLine)
        file.write(secondLine)
