from core.event_bus import EventBus

# Delete albums
def album_delete_handler(reply):

    albums = [
        {
            'artist': 'The Wurzels',
            'genre': 'Scrumpy and Western',
            'title': 'I Am A Cider Drinker',
            'price': 0.99
        },
        {
            'artist': 'Vanilla Ice',
            'genre': 'Hip Hop',
            'title': 'Ice Ice Baby',
            'price': 0.01
        },
        {
            'artist': 'Ena Baga',
            'genre': 'Easy Listening',
            'title': 'The Happy Hammond',
            'price': 0.50
        },
        {
            'artist': 'The Tweets',
            'genre': 'Bird related songs',
            'title': 'The Birdy Song',
            'price': 1.20
        }
    ]

    # Insert albums - in real life 'price' would probably be stored in a different collection, but, hey, this is a demo.
    for i in range(0, len(albums)):
        EventBus.send('vertx.mongopersistor', {
            'action': 'save',
            'collection': 'albums',
            'document': albums[i]
        })

EventBus.send('vertx.mongopersistor', {'action': 'delete', 'collection': 'albums', 'matcher': {}}, album_delete_handler)


# Delete users
def user_delete_handler(reply):
    # And a user
    EventBus.send('vertx.mongopersistor', {
        'action': 'save',
        'collection': 'users',
        'document': {
            'firstname': 'Tim',
            'lastname': 'Fox',
            'email': 'tim@localhost.com',
            'username': 'tim',
            'password': 'password'
        }
    })
EventBus.send('vertx.mongopersistor', {'action': 'delete', 'collection': 'users', 'matcher': {}}, user_delete_handler)




