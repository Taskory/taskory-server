import React, {useEffect, useState} from "react";
import '../../calendar.css';
import {useCookies} from "react-cookie";
import {tagInterface} from "./TagInterface";


interface TagModalProps {
  closeModal: () => void,
}

export const TagModal: React.FC<TagModalProps> = ({closeModal}) => {
  const [editingTagId, setEditingTagId] = useState<number | null>(null);
  const [tags, setTags] = useState<tagInterface[]>([]);
  const [preTags, setPreTags] = useState<tagInterface[]>([]);
  const [inputTag, setInputTag] = useState<tagInterface>({
    id: null,
    name: ""
  });
  const [cookies] = useCookies()
  const fetchGetTags = () => {
    fetch('http://localhost:8080/api/v1/task/tag', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': "Bearer " + cookies.token,
      },
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        ////////////////////////////////////////////////////////////////////////
        // test 용
        console.log(data);
        setTags(data);
        setPreTags(data);
        ////////////////////////////////////////////////////////////////////////
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }
  useEffect(() => {
    fetchGetTags();
  }, [cookies.token]);


  const handleCloseModal = () => {
    closeModal();
  };

  const handleCreate = () => {
    if (!inputTag) return;
    if (inputTag.name.length === 0) {
      alert("내용을 입력해주세요.")
      return;
    }

    fetch('http://localhost:8080/api/v1/task/tag', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': "Bearer " + cookies.token,
      },
      body: JSON.stringify(inputTag),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        // test 용
        fetchGetTags();
        console.log(data);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  };

  const handleUpdate = (tag: tagInterface) => {
    fetch('http://localhost:8080/api/v1/task/tag', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': "Bearer " + cookies.token,
      },
      body: JSON.stringify(tag),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        // test 용
        fetchGetTags();
        setEditingTagId(null);
        console.log(data);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

  const handleDelete = (id: number | null) => {
    if (id) {
      fetch('http://localhost:8080/api/v1/task/tag/' + id , {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': "Bearer " + cookies.token,
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          fetchGetTags();
          return response.json();
        })
        .catch(error => {
          console.error('Error:', error);
        });
    }
  }

  const handleTagChange = (tagId: number | null, newName: string) => {
    if (!tagId) {
      return;
    }
    const updatedTags = tags.map(tag => {
      if (tag.id === tagId) {
        // 수정할 태그를 찾아 새로운 이름으로 업데이트합니다.
        return { ...tag, name: newName };
      }
      return tag;
    });
    setTags(updatedTags);
  };

  const handleCancel = (id: number | null) => {
    if (!id) {
      return;
    }
    setEditingTagId(null);
    setTags(preTags);

  }
  return (
    <>
      <div className="modal modal-open">
        <div className="modal-box">
          <p className="text-lg font-bold mb-4">Tag Manage</p>
          <button className="btn btn-sm btn-circle absolute right-4 top-4 text-lg font-bold bg-red-400"
                  onClick={handleCloseModal}>X
          </button>
          <form method="dialog">
            <div className="w-full input input-bordered flex items-center mb-4">
              <p className="font-semibold mx-2">#</p>
              <input className="w-full " type="text" name="input_tag" id="input_tag" value={inputTag.name}
                     onChange={(e) => setInputTag({...inputTag, name: e.target.value})}
                     placeholder="Type here"
                     disabled={(editingTagId !== null)}
              />
              <button className="btn btn-sm font-bold bg-blue-300 mx-2" onClick={handleCreate}>Add</button>
            </div>
            <div>
              {tags.map((tag) => (
                <div key={tag.id} className="w-full input input-bordered flex items-center">
                  <div className="flex flex-grow items-center">
                    <p className="font-semibold mx-2">#</p>
                    <input className="font-semibold my-2 w-full" value={tag.name} id={tag.name} disabled={editingTagId !== tag.id} onChange={(e) => handleTagChange(tag.id, e.target.value)}/>
                  </div>
                  <div className="flex">
                    {(editingTagId !== tag.id) ? (
                      <>
                        <button className="btn btn-sm font-bold bg-yellow-100" onClick={() => setEditingTagId(tag.id)}>Update</button>
                        <button className="btn btn-sm font-bold bg-red-300" onClick={() => handleDelete(tag.id)}>Delete</button>
                      </>
                    ) : (
                      <>
                        <button className="btn btn-sm font-bold bg-blue-300" onClick={() => handleUpdate(tag)}>Save</button>
                        <button className="btn btn-sm font-bold bg-gray-300" onClick={() => handleCancel(tag.id)}>Cancel</button>
                      </>
                    )}

                  </div>
                </div>
              ))}

            </div>

          </form>
        </div>
      </div>
    </>
  );
};


