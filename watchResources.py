import os
import shutil
import time
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

class JsonFileHandler(FileSystemEventHandler):
    def __init__(self, source_dir, target_dir):
        self.source_dir = source_dir
        self.target_dir = target_dir
        self.last_modified_times = {}

    def on_modified(self, event):
        # Check if the modified file is a .json file
        if event.src_path.endswith('.json'):
            current_time = time.time()
            # Get the last modified time for the file
            last_modified_time = self.last_modified_times.get(event.src_path, 0)

            # If the file was modified more than 1 second ago, copy it
            if current_time - last_modified_time > 1:
                self.copy_file(event.src_path)
                self.last_modified_times[event.src_path] = current_time

    def copy_file(self, src_path):
        # Get the relative path of the file
        relative_path = os.path.relpath(src_path, self.source_dir)
        target_path = os.path.join(self.target_dir, relative_path)

        # Create target directory if it doesn't exist
        os.makedirs(os.path.dirname(target_path), exist_ok=True)

        # Copy the file
        shutil.copy2(src_path, target_path)
        print(f'Copied: {src_path} to {target_path}')

if __name__ == "__main__":
    source_directory = r'D:\Eclipse\workspaces\minecraft\Fogy\src\main\resources\data\fogy'
    target_directory = r'D:\Eclipse\workspaces\minecraft\Fogy\build\sourcesSets\main\data\fogy'

    event_handler = JsonFileHandler(source_directory, target_directory)
    observer = Observer()
    observer.schedule(event_handler, source_directory, recursive=True)  # Set recursive to True

    print("Monitoring for changes in JSON files...")
    observer.start()

    try:
        while True:
            pass  # Keep the script running
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
