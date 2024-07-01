import React from 'react';
import {AppRouter} from "./router/AppRouter";
import {SidebarStateProvider} from "./context/SidebarStateContext";
import {CalendarViewProvider} from "./context/CalendarViewContext";

const App = () => {

    return (
        <SidebarStateProvider>
            <CalendarViewProvider>
                <AppRouter/>
            </CalendarViewProvider>
        </SidebarStateProvider>
    );
}
export default App;
