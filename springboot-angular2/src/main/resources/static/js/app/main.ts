/**
 * Created by jojoldu@gmail.com on 2017. 3. 14.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {AppModule} from './app.module';

platformBrowserDynamic().bootstrapModule(AppModule)
    .then(success => console.log('Bootstrap successfully!'))
    .catch(err => console.log(err));